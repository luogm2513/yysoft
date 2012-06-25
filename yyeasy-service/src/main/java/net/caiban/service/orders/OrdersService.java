/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-23
 */
package net.caiban.service.orders;

import net.caiban.domain.orders.LogisticsDetails;
import net.caiban.domain.orders.Orders;
import net.caiban.domain.orders.SaleDispute;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.OrdersDto;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface OrdersService {

	
	/**
	 * 查找订单信息
	 * @param ordersDto:查询条件,不能为空<br/>
	 * 		ordersDto.orders.eid:被查询网站ID号,不可以为null
	 * @param page:分页信息,不能为null
	 * @return 
	 */
	public Paging pageOrdersDto(OrdersDto ordersDto, Paging page);
	
	/**
	 * 创建新订单信息(简单)
	 * @param orders:待创建的订单信息<br/>
	 * 		orders.eid:创建物流信息的网店,不能为null<br/>
	 * 		orders.uid:创建物流信息的用户信息,不能为null<br/>
	 * 		orders.orderNo:订单号,如果为null,则由系统自动生成(生成规则:时间戳+X位随机数字)<br/>
	 * 		orders.orderStatus:订单状态,为null时则初始化为默认状态:{@link net.caiban.persist.orders.OrdersDao#DEFAULT_ORDER_STATUS})<br/>
	 * 		orders.totalProductCash:由系统根据订单中的产品计算出来的产品金额,创建订单信息时不记录此信息<br/>
	 * 		orders.payment:订单支付方式,默认为:{@link net.caiban.persist.orders.OrdersDao#DEFAULT_PAYMENT}<br/>
	 * 		orders.buyerId:理论上每个订单必需有买家信息,但可以buyer_id可以默认为{@link net.caiban.persist.orders.OrdersDao#DEFAULT_BUYER},由用户自己对订单客户进行跟踪及维护关系
	 * @return 创建成功后返回新增物流信息的ID编号
	 */
	public Integer createSimpleOrders(Orders orders);
	
	/**
	 * 批量删除订单,只有订单状态小于{@link net.caiban.persist.orders.OrdersDao#ORDER_STATUS_DELETE_MAX}时可以被删除,其他订单只能标记交易完成,删除前需要检查订单的状态
	 * @param ordersArray:待删除的订单信息,不能为null
	 * @return 订单删除成功的数量
	 */
	public Integer batchDeleteOrders(Integer[] ordersArray);
	
	/**
	 * 通过订单ID查询一个订单信息
	 * @return
	 */
	public OrdersDto listOneOrdersById(Integer id);
	
	/**
	 * 通过订单号查询一个订单信息
	 * @return
	 */
	public OrdersDto listOneOrdersByOrderNo(String orderNo);
	
	/**
	 * 更新订单基本信息
	 * @param orders:待更新的订单基本信息,不能为null<br/>
	 * 		属性uid,eid,gmtcreate不更新,orderNo,gmtOrder,order_status不更新
	 *  
	 */
	public void updateSimpleOrders(Orders orders);
	
	/**
	 * 单独更新订单状态
	 * @param status:待更新的状态,不能为null
	 * @param id:待更新的订单,不能为null
	 * @param eid:待更新的订单所在的eshop,不能为null
	 */
	public void updateOrderStatusById(Short status,Integer id, Integer eid);
	
	/**
	 * 订单发货,更新订单状态，并且增加一条发货记录，发货后订单的状态将被改变
	 * @param logisticsDetails:发货的详细信息，不能为null<br/>
	 * 		logisticsDetails.orderId:发货的订单,不能为null<br/>
	 * 		logisticsDetails.logisticsId:发货时使用的物流渠道<br/>
	 * 		logisticsDetails.expressNo:发货时需要填写发货单号,但不是必填项,建议填写<br/>
	 * 		logisticsDetails.gmtSend:发货时间,由用户填写,表示货物发出时间,而非发货信息保存时间<br/>
	 * 		logisticsDetails.eid:网店id，不能为null<br/>
	 * 		logisticsDetails.uid:操作者的ID，不能为null
	 * @return
	 */
	public Integer deliveryOrders(LogisticsDetails logisticsDetails);
	
	/**
	 * 到货确认，更新床单状态，更新物流信息的到货时间，到货后订单状态将被改变
	 * @param logisticsDetails:到货确认信息,不能为null<br/>
	 * 		logisticsDetails.eid:网店id，不能为null<br/>
	 * 		logisticsDetails.gmtReceive:到货时间,不能为null,
	 * @return
	 */
	public Integer arrivalConfirm(LogisticsDetails logisticsDetails);
	
	/**
	 * 订单交易失败,将订单转到纠纷处理流程
	 * @param saleDispute:纠纷信息,不能为null<br/>
	 * 		saleDispute.orderId:失败的订单ID,不能为null<br/>
	 * 		saleDispute.eid:网店ID,不能为null
	 * @return
	 */
	public Integer failureOrders(SaleDispute saleDispute);
	
}
