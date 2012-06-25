/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-7-11
 */
package net.caiban.persist.orders;

import net.caiban.domain.orders.LogisticsDetails;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface LogisticsDetailsDao {

	/**
	 * 创建发货记录
	 * @param details:发货信息,不能为null<br />
	 * 		details.eid:网店ID,不能为null<br />
	 * 		details.uid:操作者的ID,不能为null<br />
	 * 		details.gmtSend:发货时间,不能为null
	 * @return
	 */
	public Integer createLogisticsDetails(LogisticsDetails details);
	
	/**根据订单,更新发货信息
	 * @param details:待更新的发货信息<br />
	 * 		details.eid:网店ID,不能为null<br />
	 * 		details.ordersId:订单ID,不能为null
	 * @return
	 */
	public Integer updateLogisticsDetailsByOrdersId(LogisticsDetails details);
}
