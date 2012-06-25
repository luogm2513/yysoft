package net.caiban.util;


public class IdCreateUtil {
	
	/**
	 * desc：用于产生n位随机字符串
	 * @param n
	 * @return
	 */
	public static String createId(Integer n){
		/*   利用Random()方法（函数）产生3位的随机代码，使得用户密码更安全   */   
        int   randomIndex   =   -1;   
        int   i=-1;   
        String   randomID   =   "";   
        String[] randomElement   =   
            {   
        		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
                "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
                "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"   
            };

        for(i=0;i<n;i++)
            {   
              /*   利用random()方法（函数）产生一个随机的整型数，用来确定字典数组的对应元素   */   
              randomIndex   =   ((new   Double(Math.random()   *   998)).intValue())   %   36;   
              randomID   =   String.valueOf(randomElement[randomIndex])   +   randomID;   
            } 
        return randomID;
	}
	
	public static void main(String[] args){
		System.out.println(IdCreateUtil.createId(6));
	}
}
