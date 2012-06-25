<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <title>${systemName } - 物流信息查询</title>
    <%@ include file="/WEB-INF/views/common/site/import.jsp"  %>
    <%@ include file="/WEB-INF/views/common/site/content.jsp"  %>
    <style type="text/css" media="screen">
		#div-result {margin-top: 20px; border-top: #999 solid 1px; padding-top: 10px;}
	</style>
	
	<script type="text/javascript">
		$(document).ready(function(){
			var _param="logistics.eid=<s:property value='logistics.eid' default='0' />&q=<s:property value='q' default='' />";
			_param = encodeURI(_param);
			_param = encodeURI(_param);
			var myconfig={
				pageSize:<s:property value="page.limit" default="1"/>,
				totalRecords:<s:property value="page.totals" default="0"/>,
				startIndex:<s:property value="page.start" default="0"/>,
				url:"${contextPath}/s/logistics.act",
				pfx:"page.",
				contenter:["pages"],
				template:{firstPageLabel:"首页",previousPageLabel:"上一页",nextPageLabel:"下一页",lastPageLabel:"末页"},
				param:_param
			};
			pageNavBar(myconfig);
		});
	</script>
  </head>
  
  <body>
	<div id="hd">
   		<div style="border-bottom: #097004 2px solid;">
   		<div style="font-size:22px;font-weight: bold;font-family: Georgia; color: #097004;margin-top: 20px;">
   			${systemName } - 物流信息查询
   		</div>
   		</div>
   	</div>
   	
	<s:form id="searchForm" namespace="/s" theme="simple" action="logistics">
	物流信息：<s:textfield name="q" id="query"></s:textfield>
	<s:hidden name="logistics.eid" id="eid"></s:hidden>
	<s:submit value="搜索"></s:submit>
	<div id="div-result">
			<div class="s-body">
				<table width="100%">
					<tr align="center" bgcolor="#666" style="color:#fff">
						<td>物流名称</td>
						<td>电话</td>
						<td>物流地址</td>
						<td>路线</td>
						<td>到货区域</td>
					</tr>
					<s:iterator id="list" value="page.records" status="idx">
					<tr>
						<td><b>${list.name}</b></td>
						<td>${list.phone}</td>
						<td>${list.address}</td>
						<td>${list.routes}</td>
						<td>${list.arrivalArea}</td>
					</tr>
					</s:iterator>
				</table>
			</div>
	</div>
	</s:form>
	<br/>
	<div id="pages" ></div>
  </body>
</html>
