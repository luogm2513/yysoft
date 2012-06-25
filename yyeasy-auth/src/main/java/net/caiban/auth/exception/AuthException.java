/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-28
 */
package net.caiban.auth.exception;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class AuthException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1889970457336771338L;

	public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(String message){
        super(message);
    }
}
