/**
 * Copyright 2010 ASTO.
 * All right reserved.
 * Created on 2010-3-11
 */
package net.caiban.auth.impl;

import java.util.ArrayList;
import java.util.List;

import net.caiban.auth.RightDao;
import net.caiban.auth.RightService;
import net.caiban.auth.domain.AuthRight;
import net.caiban.auth.dto.ExtTreeNode;
import net.caiban.auth.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("rightService")
public class RightServiceImpl implements RightService {

	@Autowired
	private RightDao rightDao;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("static-access")
	public Integer createRight(AuthRight right) {
		Assert.notNull(right, messageSource.getMessage("assert.notnull", new String[]{"right"}, null));
		if(right.getParentId()==null){
			right.setParentId(rightDao.DEFAULT_RIGHT_ROOT);
		}
		return rightDao.createRight(right);
	}

	public Integer deleteRight(Integer rightId) {
		Assert.notNull(rightId, messageSource.getMessage("assert.notnull", new String[]{"rightId"}, null));
		return rightDao.deleteRight(rightId);
	}

	public AuthRight listOneRightViaId(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return rightDao.listOneRightViaId(id);
	}

	public List<AuthRight> listRightInRole(Integer roleId, Integer parentId) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		return rightDao.listRightByRole(roleId, parentId, true);
	}

	public List<AuthRight> listRightNotInRole(Integer roleId,
			Integer parentId) {
		Assert.notNull(roleId, 
				messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		return rightDao.listRightByRole(roleId, parentId, false);
	}

	public List<AuthRight> listRightViaLR(Integer l, Integer r) {
		return rightDao.listRightViaLR(l, r);
	}

	public List<AuthRight> listRightViaParent(Integer parentId) {
		return rightDao.listRightViaParent(parentId);
	}

	public void updateRightBaseInfo(AuthRight right) {
		Assert.notNull(right, 
				messageSource.getMessage("assert.notnull", new String[]{"right"}, null));
		Assert.notNull(right.getId(), 
				messageSource.getMessage("assert.notnull", new String[]{"right.id"}, null));
		rightDao.updateRightBaseInfo(right);
	}

	public List<ExtTreeNode> changeRightListToTreeNode(List<AuthRight> rightList, Boolean check) {
		Assert.notNull(rightList, 
				messageSource.getMessage("assert.notnull", new String[]{"rightList"}, null));
		if(check == null){
			check = DEFAULT_CHECKED_USE;
		}
		List<ExtTreeNode> nodeList= new ArrayList<ExtTreeNode>();
		for(AuthRight right:rightList){
			if(right.getMenu()!=null && !"".equals(right.getMenu())){
				ExtTreeNode node=new ExtTreeNode();
				node.setData(right.getId());
				node.setText(right.getName());
				node.setLeaf((right.getR()-right.getL()-1)/2 == 0);
				node.setMenuUrl(right.getMenuUrl());
//			node.setCls(right.getMenuCss());
				if(check){
					node.setChecked(DEFAULT_CHECKED_USE);
				}
				nodeList.add(node);
			}
		}
		return nodeList;
	}

}
