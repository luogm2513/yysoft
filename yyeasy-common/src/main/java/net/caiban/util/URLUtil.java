package net.caiban.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class URLUtil {

	/**
	   * 转换编码 ISO-8859-1到GB2312
	   * @param text
	   * @return
	   */
	public static String ISO2GB(String text) {
	    String result = "";
	
	    try {
	        result = new String(text.getBytes("ISO-8859-1"), "GB2312");
	    } catch (UnsupportedEncodingException ex) {
	        result = ex.toString();
	    }
	
	    return result;
	}

	/**
	 * 转换编码 GB2312到ISO-8859-1
	 * @param text
	 * @return
	 */
	public static String GB2ISO(String text) {
	    String result = "";
	
	    try {
	        result = new String(text.getBytes("GB2312"), "ISO-8859-1");
	    } catch (UnsupportedEncodingException ex) {
	        ex.printStackTrace();
	    }
	
	    return result;
	}

	/**
	 * 编码是否有效
	 * @param text
	 * @return
	 */
	private static boolean Utf8codeCheck(String text) {
	    String sign = "";
	    if (text.startsWith("%e")) {
	        for (int i = 0, p = 0; p != -1; i++) {
	            p = text.indexOf("%", p);
	            if (p != -1) {
	                p++;
	            }
	            sign += p;
	        }
	    }
	
	    return sign.equals("147-1");
	}
	
	/**
	 * 是否Utf8Url编码
	 * @param text
	 * @return
	 */
	public static boolean isUtf8Url(String text) {
	    text = text.toLowerCase();
	
	    int p = text.indexOf("%");
	
	    if ((p != -1) && ((text.length() - p) > 9)) {
	        text = text.substring(p, p + 9);
	    }
	
	    return Utf8codeCheck(text);
	}

	/**
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map getUrlParameters(String url) {
	    Map<String,String> map = new HashMap<String,String>();
	    int pos = url.indexOf("?");
	    url = url.substring(pos + 1);
	    String[] paras = url.split("&");
	    for (String para : paras) {
	        pos = para.indexOf("=");
	        if (pos != -1) {
	            map.put(para.substring(0, pos), para.substring(pos + 1));
	        }
	    }
	    return map;
	}

	/**
	 * @param uri
	 * @return
	 */
	public static String getUrlFileName(String uri) {
	    int pos = uri.indexOf("?");
	
	    if (pos > 0) {
	        return uri.substring(0, pos);
	    }
	
	    return uri;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	    getUrlParameters("http://sss.com.com/aa.do");
	
	    String stest = "中文1234 abcd[]()<+>,.~\\";
	    System.out.println(stest);
	}
}
