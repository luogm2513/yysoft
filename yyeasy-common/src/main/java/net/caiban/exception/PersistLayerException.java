/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-13
 */
package net.caiban.exception;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class PersistLayerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1297824913537007233L;

	public PersistLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistLayerException(String message){
        super(message);
    }
}
