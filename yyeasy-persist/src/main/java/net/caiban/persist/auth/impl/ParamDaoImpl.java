/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-9
 */
package net.caiban.persist.auth.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.domain.auth.Param;
import net.caiban.persist.auth.ParamDao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("paramDao")
public class ParamDaoImpl extends SqlMapClientDaoSupport implements ParamDao {

	@SuppressWarnings("unchecked")
	public List<Param> listAllParam(Short used) {
		Map root = new HashMap();
		root.put("used", used);
		return getSqlMapClientTemplate().queryForList("param.listAllParam", root);
	}

}
