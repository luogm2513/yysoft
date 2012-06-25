package net.caiban.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.caiban.action.util.AdminConst;
import net.caiban.action.util.CacheConstFacade;
import net.caiban.auth.dto.SessionUserDto;
import net.caiban.util.StringUtil;

public class AuthorizeFilter implements Filter {

	private Set<String> noLoginPage;
	private Set<String> noAuthPage;
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest rq, ServletResponse rp,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest) rq;
		HttpServletResponse response = (HttpServletResponse) rp;
		// 各类变量设置
		initPage(request);
		
		//过滤权限  - 过滤非控制页面
		String uri=request.getRequestURI();
		String path=request.getContextPath();
		path = path==null?"":path;
		String url = path+"/login.act";
		do{
			if(canFilter(noLoginPage, path, uri)){
				filterChain.doFilter(request, response);
				return ;
			}
			
			SessionUserDto userDto= (SessionUserDto) getCachedSession(request, AdminConst.SESSION_USER);
			if(userDto==null || userDto.getUser()==null){  //未登录情况
				break;
			}
			
			if(canFilter(noAuthPage, path, uri)){ 
				filterChain.doFilter(request, response);
				return ;
			}
			
			if(canFilter(request, path, uri)){  //权限控制
				filterChain.doFilter(request, response);
				return ;
			}else{
				url = path+"/forbiden.act";
				break;
			}
		}while(false);
		
		//AJAX请求错误处理
		if(request.getHeader("x-requested-with")!=null   
				&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){  
			response.setHeader("sessionstatus","timeout");
			response.setHeader("redirectUrl",url);
			return ;
		}
		response.sendRedirect(url);  //普通http请求错误处理
		return ;
	}

	public void init(FilterConfig config) throws ServletException {
		String tmp[]=null;
		noLoginPage = new HashSet<String>();
		String e1=config.getInitParameter("noLoginPage");
		if(StringUtil.isNotEmpty(e1)){
			tmp=e1.split("\\|");
			for(String ex:tmp){
				noLoginPage.add(ex);
			}
		}
		
		noAuthPage = new HashSet<String>();
		String e2=config.getInitParameter("noAuthPage");
		if(StringUtil.isNotEmpty(e2)){
			tmp=e2.split("\\|");
			for(String ex:tmp){
				noAuthPage.add(ex);
			}
		}
	}
	
	private void initPage(HttpServletRequest request){
		request.setAttribute("contextPath", request.getContextPath());
		request.setAttribute("systemName", "YYSoft Easyadmin");
		request.setAttribute("loginUser", getCachedSession(request, AdminConst.SESSION_USER));
		request.setAttribute("imgserver", CacheConstFacade.getStringValue("imgserver"));
		request.setAttribute("debug", CacheConstFacade.getStringValue("debug"));
	}

	private boolean canFilter(Set<String> exclude,String path,String uri){
		for(String url:exclude){
			url=path+url;
			if(url.startsWith("*")){
				if(uri.endsWith(url.substring(1))) 
					return true;
			}else if(url.endsWith("*")){
				if(uri.startsWith(url.substring(0, url.length()-1))) 
					return true;
			}else{
				if(url.equals(uri))
					return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean canFilter(HttpServletRequest request,String path,String uri){
		List<String> rightList = (List<String>) getCachedSession(request, AdminConst.SESSION_AUTH);
		String query = request.getQueryString();
		if(path != null && uri.startsWith(path)){
			uri=uri.substring(path.length());
			String url = uri+(query == null ? "" : "?"+query);
			for(String right:rightList){
				if(url.matches(right)){
					return true;
				}
			}
		}
		return false;
		
//		List<AuthRight> right = (List<AuthRight>) request.getSession().getAttribute(HouseConst.SESSION_RIGHT);
//		if(path != null && path.trim().length() >= 0
//				&& uri.startsWith(path)){
//			uri=uri.substring(path.length());
//			String query = request.getQueryString();
//			String url = uri+(query == null ? "" : "?"+query); 
//			for(AuthRight r:right){
//				String reg = r.getRightContent() ==null ? "" : r.getRightContent();
//				if(reg.trim().length()>0){
//					if(url.matches(reg)){
//						return true;
//					}
//				}
//			}
//		}
	}

	private Object getCachedSession(HttpServletRequest request, String key){
		return request.getSession().getAttribute(key);
	}
}
