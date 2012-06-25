/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-18
 */
package net.caiban.auth.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class ExtTreeNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -873977581992227957L;
	
	private String id;
	private String text;
	private String cls;
	private Boolean leaf;
	private Boolean checked;
	private Object data;
	private String menuUrl;
	@SuppressWarnings("unchecked")
	private List children;
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the cls
	 */
	public String getCls() {
		return cls;
	}
	/**
	 * @param cls the cls to set
	 */
	public void setCls(String cls) {
		this.cls = cls;
	}
	/**
	 * @return the children
	 */
	@SuppressWarnings("unchecked")
	public List getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	@SuppressWarnings("unchecked")
	public void setChildren(List children) {
		this.children = children;
	}
	/**
	 * @return the leaf
	 */
	public Boolean getLeaf() {
		return leaf;
	}
	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	/**
	 * @return the menuUrl
	 */
	public String getMenuUrl() {
		return menuUrl;
	}
	/**
	 * @param menuUrl the menuUrl to set
	 */
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
}
