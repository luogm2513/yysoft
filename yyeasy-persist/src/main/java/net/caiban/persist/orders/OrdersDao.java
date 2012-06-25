/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-25
 */
package net.caiban.persist.orders;

import java.util.List;

import net.caiban.domain.orders.Orders;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.OrdersDto;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface OrdersDao {

	/**
	 * 默认订单状态:0 新订单
	 */
	final static short DEFAULT_ORDER_STATUS = 0;
	
	/**
	 * 可以被删除的订单,其状态必需小于指定值
	 */
	final static short ORDER_STATUS_DELETE_MAX = 11;
	/**
	 * 默认支付方式:支付宝
	 */
	final static String DEFAULT_PAYMENT = "alipay";
	/**
	 * 默认的订单买家
	 */
	final static int DEFAULT_BUYER = 0;
	
	
	
	/**
	 * 查找订单信息
	 * @param ordersDto:查询条件,不能为空<br/>
	 * 		ordersDto.orders.eid:被查询网站ID号,不可以为null
	 * @param page:分页信息,不能为null
	 * @return 
	 */
	public List<OrdersDto> pageOrdersDto(OrdersDto ordersDto, Paging page);
	
	/**
	 * 统计记录总数
	 * @param ordersDto:查询条件,不能为空<br/>
	 * 		ordersDto.orders.eid:被查询网站ID号,不可以为null
	 * @return 
	 */
	public Integer countPageOrders(OrdersDto ordersDto);
	
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
	 * 		属性uid,eid,gmtcreate不更新,orderNo,totalProductCash,gmtOrder,order_status不更新
	 *  
	 */
	public void updateSimpleOrders(Orders orders);
	
	/**
	 * 单独更新订单状态
	 * @param status:待更新的状态,不能为null
	 * @param id:待更新的订单,不能为null
	 * @param eid:待更新的订单所在的eshop,不能为null
	 */
	public Integer updateOrderStatusById(Short status,Integer id, Integer eid);
}
