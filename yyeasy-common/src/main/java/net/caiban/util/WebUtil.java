package net.caiban.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WebUtil {

	public static void main(String[] args) {
		WebUtil.getHtml("http://www.google.com", "utf-8");
	}

	public static String getHtml(String url,String charset){
		if(url==null || "".equals(url)) return null;
		try {
			URL source = new URL(url);
			URLConnection conn = source.openConnection();
			conn.setDoOutput(true);
			InputStream content = null;
			content = source.openStream();
			String getWeb = webHtml(content, charset);
			return getWeb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String webHtml(InputStream in, String charset)
			throws IOException {
		StringBuffer s = new StringBuffer();
		if (charset == null || "".equals(charset)) {
			charset = "utf-8";
		}
		String rLine = null;
		BufferedReader bReader = new BufferedReader(new InputStreamReader(in,
				charset));
		while ((rLine = bReader.readLine()) != null) {
			String tmp_rLine = rLine;
			int str_len = tmp_rLine.length();
			if (str_len > 0) {
				s.append(tmp_rLine);
			}
			tmp_rLine = null;
		}
		in.close();  //in.close()后reader也被close了
		return s.toString();
	}
	
	public static InputStream getStream(String url,String charset){
		if(url==null || "".equals(url)) return null;
		try {
			URL source = new URL(url);
			URLConnection conn = source.openConnection();
			conn.setDoOutput(true);
			return source.openStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
