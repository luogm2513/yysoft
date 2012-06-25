/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-28
 */
package net.caiban.service.orders;

import net.caiban.domain.orders.Logistics;
import net.caiban.dto.Paging;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface LogisticsService {

	/**
	 * 查找某个网店的物流信息
	 * @param logistics:查询条件,不能为空<br/>
	 * 		logistics.name:物流名称,可作为查询条件,查出与其前后匹配的物流信息<br/>
	 * 		logistics.address:物流地址,可作为查询条件,查出与其前后匹配的物流信息<br/>
	 * 		logistics.routes:运输路线,可作为查询条件,查出与其前后匹配的物流信息<br/>
	 * 		logistics.arrivalArea:到货区,可作为查询条件,查询出与其前后匹配的物流信息<br/>
	 * 		logistics.eid:被查询网站ID号,不可以为null
	 * @param page:分页信息,不能为null
	 * @return
	 */
	public Paging pageLogisticsWithEid(Logistics logistics, Paging page);
	
	/**
	 * 网店用户自己创建的物流信息
	 * @param logistics:待创建的物流信息<br/>
	 * 		logistics.eid:创建物流信息的网店,不能为null<br/>
	 * 		logistics.uid:创建物流信息的用户信息,不能为null<br/>
	 * 		logistics.name:物流名称,不能为null
	 * @return 创建成功后返回新增物流信息的ID编号
	 */
	public Integer createLogisticsByEid(Logistics logistics);
	
	/**
	 * 网店用户自己批量删除物流信息
	 * @param logisticsArray:待删除的物流信息,不能为null
	 */
	public void batchDeleteLogisticsByEid(Integer[] logisticsArray);
	
	/**
	 * 通过物流信息ID查找出物流修
	 * @return
	 */
	public Logistics listOneLogisticsById(Integer id);
	
	/**
	 * 更新物流信息
	 * @param logistics:待更新的物流信息,不能为null<br/>
	 * 		属性uid,eid,gmtcreate不更新
	 */
	public void updateLogisticsById(Logistics logistics);
	
	/**
	 * 前台页面搜索物流信息的功能
	 * @param logistics:搜索中可能会用到的准确查询条件
	 * @param q:任意的查询条件
	 * @param page:分页信息
	 * @return
	 */
	public Paging search(Logistics logistics, String q,Paging page);
	
}
