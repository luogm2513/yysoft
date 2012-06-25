/**
 * Copyright 2010 ASTO.
 * All right reserved.
 * Created on 2010-3-11
 */
package net.caiban.auth.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.auth.RightDao;
import net.caiban.auth.domain.AuthRight;
import net.caiban.auth.exception.AuthException;
import net.caiban.auth.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("rightDao")
public class RightDaoImpl extends SqlMapClientDaoSupport implements RightDao {

	@Autowired
	private MessageSource messageSource;
	
	public Integer createRight(AuthRight right) {
		Assert.notNull(right, messageSource.getMessage("assert.notnull", new String[]{"right"}, null));
		Assert.notNull(right.getParentId(), messageSource.getMessage("assert.notnull", new String[]{"parentid"}, null));
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
			AuthRight parent=(AuthRight) getSqlMapClientTemplate().queryForObject("authRight.listRightById",right.getParentId());
			if(parent==null){
				Integer maxnum=(Integer) getSqlMapClientTemplate().queryForObject("authRight.queryMaxRight");
				if(maxnum==null){
					maxnum=0;
				}
				right.setL(maxnum+1);
				right.setR(maxnum+2);
			}else{
				right.setL(parent.getR());
				right.setR(parent.getR()+1);
				getSqlMapClientTemplate().update("authRight.updateLeftForCreate",parent.getR());
				getSqlMapClientTemplate().update("authRight.updateRightForCreate",parent.getR());
			}
			impact=(Integer) getSqlMapClientTemplate().insert("authRight.createRight", right);
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new AuthException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer deleteRight(Integer rightId) {
		Assert.notNull(rightId, messageSource.getMessage("assert.notnull", new String[]{"rightId"}, null));
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
			AuthRight parent=(AuthRight) getSqlMapClientTemplate().queryForObject("authRight.listRightById",rightId);
			Map<String, Integer> rolerightMap=new HashMap<String, Integer>();
			rolerightMap.put("left", parent.getL());
			rolerightMap.put("right", parent.getR());
			impact+=getSqlMapClientTemplate().delete("authRight.deleteRoleRightByParentRight", rolerightMap);
			impact+=getSqlMapClientTemplate().delete("authRight.deleteRightByLR", rolerightMap);
			impact+=getSqlMapClientTemplate().update("authRight.updateLeftForDelete", rolerightMap);
			impact+=getSqlMapClientTemplate().update("authRight.updateRightForDelete", rolerightMap);
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new AuthException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public AuthRight listOneRightViaId(Integer id) {
		return (AuthRight) getSqlMapClientTemplate().queryForObject("authRight.listRightById",id);
	}

	@SuppressWarnings("unchecked")
	public List<AuthRight> listRightViaParent(Integer parentId) {
		if(parentId==null){
			parentId=DEFAULT_RIGHT_ROOT;
		}
		return getSqlMapClientTemplate().queryForList("authRight.listRightByParent", parentId);
	}

	public void updateRightBaseInfo(AuthRight right) {
		Assert.notNull(right, messageSource.getMessage("assert.notnull", new String[]{"right"}, null));
		Assert.notNull(right.getId(), messageSource.getMessage("assert.notnull", new String[]{"right.id"}, null));

		getSqlMapClientTemplate().update("authRight.updateRightBaseInfo", right);
	}

	@SuppressWarnings("unchecked")
	public List<AuthRight> listRightByRole(Integer roleId, Integer parentId, boolean inrole) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		if(parentId==null){
			parentId=DEFAULT_RIGHT_ROOT;
		}
		Map<String, Integer> root=new HashMap<String, Integer>();
		List<AuthRight> list;
		try {
			getSqlMapClient().startBatch();
			AuthRight right=(AuthRight) getSqlMapClientTemplate().queryForObject("authRight.listRightById",parentId);
			if(right==null){
				root.put("left", 0);
				root.put("right", 1+(Integer)getSqlMapClientTemplate().queryForObject("authRight.queryMaxRight"));
			}else{
				root.put("left", right.getL());
				root.put("right", right.getR());
			}
			root.put("roleid", roleId);
			if(inrole){
				list=getSqlMapClientTemplate().queryForList("authRight.listRightInRole", root);
			}else{
				list=getSqlMapClientTemplate().queryForList("authRight.listRightNotInRole", root);
			}
			getSqlMapClient().executeBatch();

		} catch (SQLException e) {
			throw new AuthException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<AuthRight> listRightViaLR(Integer left, Integer right) {
		Assert.notNull(left, messageSource.getMessage("assert.notnull", new String[]{"left"}, null));
		Assert.notNull(right, messageSource.getMessage("assert.notnull", new String[]{"right"}, null));
		Map<String, Integer> root=new HashMap<String, Integer>();
		root.put("left", left);
		root.put("right", right);
		return getSqlMapClientTemplate().queryForList("authRight.listRightByLR", root);
	}
	
	@SuppressWarnings("unchecked")
	public List<AuthRight> listUserRightByParent(String username, Integer parentId) {
		Assert.notNull(username, messageSource.getMessage("assert.notnull", new String[]{"username"}, null));
		Assert.notNull(parentId, messageSource.getMessage("assert.notnull", new String[]{"parentId"}, null));
		
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("username", username);
		root.put("parentid", parentId);
		return getSqlMapClientTemplate().queryForList("authRight.listUserRightByParent", root);
	}

	@SuppressWarnings("unchecked")
	public List<AuthRight> listUserRightByUsername(String username) {
		Assert.notNull(username, messageSource.getMessage("assert.notnull", new String[]{"username"}, null));
		return getSqlMapClientTemplate().queryForList("authRight.listUserRightByUsername", username);
	}

}
