<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <title>${systemName } - 订单管理中心</title>
    <%@ include file="/WEB-INF/views/common/import.jsp"  %>
    <%@ include file="/WEB-INF/views/common/content.jsp"  %>
    
   	<script type="text/javascript" src="${contextPath }/js/extux/StatusBar.js" ></script>
	<script type="text/javascript" src="${contextPath }/app/orders/orders.js" ></script>
	<script type="text/javascript" src="${contextPath }/app/orders/saleDispute.js" ></script>
	
	<script type="text/javascript">

		var orderStatusMenu = [
			<s:iterator id="list" value="paramList" status="idx">
			<s:if test="#idx.index > 0">,</s:if>
			{
				text:"${list.value}",
				inputValue:"${list.key}",
				checked: <s:if test="#idx.index < 3">true</s:if><s:else>false</s:else>,
				checkHandler:caiban.easyadmin.orders.changeOrdersStatus
			}
			</s:iterator>
		];

		Ext.onReady(function(){

			//需要判断用户是否拥有查看所有订单的权利，默认只显示自己添加的订单
			var grid=new caiban.easyadmin.orders.OrdersGrid({
				id:ORDERS.ORDERS_GRID,
				region:"center",
				layout:"fit"
			});
			
	//		var searchForm = new caiban.easyadmin.orders.SearchForm({
	//			title:"搜索订单",
	//			region:"north"
	//		}); 
			
			var viewport = new Ext.Viewport({
				layout:"border",
				items:[grid] //,searchForm
			});

			caiban.easyadmin.orders.changeOrdersStatus();
		});  
	</script>
    
  </head>
  
  <body>

  </body>
</html>
