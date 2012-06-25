/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-7-28
 */
package net.caiban.service.orders;

import net.caiban.domain.orders.SaleDispute;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.SaleDisputeDto;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface SaleDisputeService {

	/**更新售后纠纷处理请求基本信息
	 * @param saleDispute:纠纷信息,不能为null<br/>
	 * 		saleDispute.id:待更新的纠纷信息,不能为null<br/>
	 * 		saleDispute.eid:网店ID,不能为Null<br/>
	 * 		只更新基本信息字段(returnGoods,reparation,remark,period,serverity)
	 * @return
	 */
	public Integer updateSaleDisputeSimple(SaleDispute saleDispute);
	
	/**
	 * 更新解决状态
	 * @param status:状态,不能为null
	 * @param resolvedUid:解决的用户,不能为null
	 * @param eid:网店ID,不能为null
	 * @param id:待更新的纠纷ID,不能为null
	 * @return
	 */
	@Deprecated
	public Integer updateResolvedStatus(Short status, Integer resolvedUid, Integer eid, Integer id);
	
	/**
	 * 根据ID获取一条售后纠纷信息
	 * @param id:纠纷ID,不能为null
	 * @param eid:网店ID,不能为null
	 * @return
	 */
	public SaleDispute listOneSaleDisputeById(Integer id, Integer eid);
	
	/**
	 * 分页列出打开的纠纷信息，默认打开未处理和处理中的问题
	 * @param saleDisputeDto：查询条件，不能为null<br/>
	 * 		saleDisputeDto.saleDispute.resolved:解决状态<br/>
	 * 		saleDisputeDto.saleDispute.orderId:订单ID<br/>
	 * @return
	 */
	public Paging pageSaleDispute(SaleDisputeDto saleDisputeDto, Paging page);
	
	/**
	 * @param resolved:解决状态
	 * @param ids:需要更新的纠纷ID集合
	 * @param eid:网店ID，不能为null
	 * @return
	 */
	public Integer batchUpdateResolved(Short status, Integer[] ids, Integer eid);
	
	/**
	 * @param uid:指派对象
	 * @param ids:需要更新的纠纷ID集合
	 * @param eid:网店ID，不能为null
	 * @return
	 */
	public Integer batchUpdateResolvedUid(Integer uid, Integer[] ids, Integer eid);
	
	/**
	 * 删除指定ID的售后纠纷
	 * @param ids:指定删除的纠纷信息
	 * @param eid:网店ID，不能为null
	 * @return
	 */
	public Integer batchDeleteSaleDispute(Integer[] ids, Integer eid);
}
