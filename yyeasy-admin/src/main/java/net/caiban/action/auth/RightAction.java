/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-18
 */
package net.caiban.action.auth;

import java.util.ArrayList;
import java.util.List;

import net.caiban.action.BaseAction;
import net.caiban.auth.RightService;
import net.caiban.auth.domain.AuthRight;
import net.caiban.auth.dto.ExtTreeNode;
import net.caiban.dto.ExtResult;
import net.caiban.dto.Paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Scope("prototype")
@Service
public class RightAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1325654060736672679L;
	
	@Autowired
	private RightService rightService;
	
	private AuthRight right;
	private List<ExtTreeNode> treeNodeList;
	private Boolean checkbox;
	private ExtResult result;
	private Paging page;
	
	public RightAction(){
		right = new AuthRight();
		treeNodeList = new ArrayList<ExtTreeNode>();
		result = new ExtResult();
		page = new Paging();
	}
	
	/******************************/
	public String index(){
		return "index";
	}
	
	/**
	 * 通过父权限编号列表此权限下所有子权限信息
	 * @return
	 */
	public String listRightChild(){
		treeNodeList =rightService.changeRightListToTreeNode(
				rightService.listRightViaParent(right.getParentId()), checkbox); 
		return "exttree";
	}
	
	public String createRight(){
		if(rightService.createRight(right)>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	public String updateRight(){
		rightService.updateRightBaseInfo(right);
		result.setSuccess(true);
		return "extresult";
	}
	
	public String listOneRight(){
		List<AuthRight> l=new ArrayList<AuthRight>();
		l.add(rightService.listOneRightViaId(right.getId()));
		page.setRecords(l);
		return "extpage";
	}
	
	public String deleteRight(){
		if(rightService.deleteRight(right.getId())>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	/**
	 * @return the right
	 */
	public AuthRight getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(AuthRight right) {
		this.right = right;
	}

	/**
	 * @return the treeNodeList
	 */
	public List<ExtTreeNode> getTreeNodeList() {
		return treeNodeList;
	}

	/**
	 * @param treeNodeList the treeNodeList to set
	 */
	public void setTreeNodeList(List<ExtTreeNode> treeNodeList) {
		this.treeNodeList = treeNodeList;
	}

	/**
	 * @return the checkbox
	 */
	public Boolean getCheckbox() {
		return checkbox;
	}

	/**
	 * @param checkbox the checkbox to set
	 */
	public void setCheckbox(Boolean checkbox) {
		this.checkbox = checkbox;
	}

	/**
	 * @return the result
	 */
	public ExtResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ExtResult result) {
		this.result = result;
	}

	/**
	 * @return the page
	 */
	public Paging getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Paging page) {
		this.page = page;
	}

}
