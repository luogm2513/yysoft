package net.caiban.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * @param s
	 * @return
	 */
	public static boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!((s.charAt(i) >= '0') && (s.charAt(i) <= '9'))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param s
     * @return
     */
    public static boolean isIp(String s) {
        String strMatch = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
        Pattern ParsePattern = Pattern.compile(strMatch);
        Matcher ParseMatcher = ParsePattern.matcher(s);
        return ParseMatcher.find();
    }

    /**
     * @param s
     * @return
     */
    public static boolean isDomainName(String s) {
        String strMatch = "[a-zA-Z0-9]+([a-zA-Z0-9\\-\\.]+)?\\.(com|cn|org|net|mil|edu|COM|ORG|NET|MIL|EDU)";
        Pattern ParsePattern = Pattern.compile(strMatch);
        Matcher ParseMatcher = ParsePattern.matcher(s);
        return ParseMatcher.find();
    }

    /**
     * @param s
     * @return
     */
    public static boolean isEmail(String s) {
        String strMatch = "([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})";
        Pattern ParsePattern = Pattern.compile(strMatch);
        Matcher ParseMatcher = ParsePattern.matcher(s);
        return ParseMatcher.find();
    }
    
    /**
     * @param d
     * @param pL
     * @return
     */
    public static String getStandardDouble(double d, int pL) {
        String format = "0.";
        for (int i = 0; i < pL; i++)
            format += "0";
        return ((new DecimalFormat(format)).format(d));
    }
    
    /**
     * @param n
     * @return
     */
    public static String getRandValue(int n) {
        String sRand = "";
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
        }
        return sRand;
    }
    
    /**
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0);
    }

    /**
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 将"1,2,3"格式的字符串转换成整型数组
     * @param strArray:需要转换成数组的字符串
     * @return
     */
    public static Integer[] str2intArray(String strArray){
    	if(isEmpty(strArray)){
    		return new Integer[0];
    	}
    	String[] sl = strArray.split(",");
    	Integer[] i = new Integer[sl.length];
    	for(int ii=0,l=sl.length;ii<l;ii++){
    		if(isNumber(sl[ii])){
    			i[ii]=Integer.valueOf(sl[ii]);
    		}
    	}
    	return i;
    }
    
    /**
     * 将"1,2,3"格式的字符串转换成短整型数组
     * @param strArray:需要转换成数组的字符串
     * @return
     */
    public static Short[] str2shortArray(String strArray){
    	if(isEmpty(strArray)){
    		return new Short[0];
    	}
    	String[] sl = strArray.split(",");
    	Short[] i = new Short[sl.length];
    	for(int ii=0,l=sl.length;ii<l;ii++){
    		if(isNumber(sl[ii])){
    			i[ii]=Short.valueOf(sl[ii]);
    		}
    	}
    	return i;
    }
    
    public static String generateFileName(String fileName){
    	DateFormat format = new SimpleDateFormat("yyMMddHHmmss");   
        String formatDate = format.format(new Date());   
           
        int random = new Random().nextInt(10000);   
           
        int position = fileName.lastIndexOf(".");   
        String extension = fileName.substring(position);   
           
        return formatDate + random + extension;
    }
}
