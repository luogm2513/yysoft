/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-19
 */
package net.caiban.auth.dto;

import java.util.List;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class Paging implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 默认每页显示的记录数量
	 */
	final static int DEFAULT_LIMIT = 20;
	/**
	 * 默认起始记录
	 */
	final static int DEFAULT_START = 0;
	/**
	 * 默认按照升序(asc)排序
	 */
	final static String DEFAULT_DIR = "asc";

	private Integer totals;
	private Integer start;
	private Integer limit;
	private String sort;
	private String dir;
	private Integer recordsReturned;
	
	@SuppressWarnings("unchecked")
	private List records;

	/**
	 * @return the totals
	 */
	public Integer getTotals() {
		return totals;
	}

	/**
	 * @param totals the totals to set
	 */
	public void setTotals(Integer totals) {
		this.totals = totals;
	}

	/**
	 * @return the start
	 */
	public Integer getStart() {
		if(start==null){
			start = DEFAULT_START;
		}
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Integer start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		if(limit == null){
			limit = DEFAULT_LIMIT;
		}
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the dir
	 */
	public String getDir() {
		if(dir==null){
			dir = DEFAULT_DIR;
		}
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * @return the recordsReturned
	 */
	public Integer getRecordsReturned() {
		return recordsReturned;
	}

	/**
	 * @param recordsReturned the recordsReturned to set
	 */
	public void setRecordsReturned(Integer recordsReturned) {
		this.recordsReturned = recordsReturned;
	}

	/**
	 * @return the records
	 */
	@SuppressWarnings("unchecked")
	public List getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	@SuppressWarnings("unchecked")
	public void setRecords(List records) {
		this.records = records;
	}
	
}
