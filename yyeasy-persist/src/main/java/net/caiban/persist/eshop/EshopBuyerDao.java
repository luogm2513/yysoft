/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-9
 */
package net.caiban.persist.eshop;

import java.util.List;

import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.dto.Paging;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface EshopBuyerDao {
	
	final static Integer DEFAULT_MAX_BUYER = 20;
	/**
	 * @param buyer:查询条件,不能为null<br/>
	 * 		buyer.eid:登录用户的eshop id,不能为null
	 * @param page:分页信息,不能为null
	 * @return
	 */
	public List<EshopBuyer> pageEshopBuyer(EshopBuyer buyer, Paging page);
	
	/**
	 * 统计记录总数
	 * @param buyer:查询条件,不能为null<br/>
	 * 		buyer.eid:登录用户的eshop id,不能为null
	 * @return
	 */
	public Integer countPageEshopBuyer(EshopBuyer buyer);
	
	/**
	 * @param buyer:待创建的客户信息,不能为null
	 * 		buyer.eid:eshop id,不能为null<br/>
	 * 		buyer.uid:创建该客户信息的用户ID,不能为null
	 * @return:新创建的客户信息ID
	 */
	public Integer createEshopBuyer(EshopBuyer buyer);
	
	/**
	 * 更新网店客户信息
	 * @param buyer:待更新的信息,不能为null<br/>
	 * 		buyer.id:待更新的网店客户信息ID,不能为null<br/>
	 */
	public void updateEshopBuyer(EshopBuyer buyer);
	
	/**
	 * 批量删除网店客户信息
	 * @param idArray:ID数组
	 * @return
	 */
	public Integer batchDeleteEshopBuyerBuyIdArray(Integer[] idArray);
	
	/**
	 * 通过ID信息查找一条网店客户信息
	 * @param id:网店客户信息的ID
	 * @param eid:eshop id,不能为null
	 * @return
	 */
	public EshopBuyer listOneEshopBuyerById(Integer id,Integer eid);
	
	/**
	 * 通过name查找客户信息
	 * @param name:查询条件，不能为null
	 * @param maxNum：为null时取默认值
	 * @param eid:eshop id,不能为null
	 * @param uid:登录用户的id
	 * @return
	 */
	public List<EshopBuyer> listEshopBuyerByName(String name,Integer maxNum, Integer eid, Integer uid);
}
