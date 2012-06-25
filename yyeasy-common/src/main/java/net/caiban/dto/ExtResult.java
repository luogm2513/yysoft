/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-14
 */
package net.caiban.dto;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class ExtResult implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean success;
	private Boolean error;
	private Object data;
	
	/**
	 * @return the success
	 */
	public Boolean getSuccess() {
		if(success==null){
			success = false;
		}
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	/**
	 * @return the error
	 */
	public Boolean getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(Boolean error) {
		this.error = error;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
