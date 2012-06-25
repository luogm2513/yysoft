/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-14
 */
package net.caiban.dto;

import java.io.Serializable;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class PageInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String copyright;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the copyright
	 */
	public String getCopyright() {
		return copyright;
	}
	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
}
