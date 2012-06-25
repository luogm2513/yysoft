<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <title>${systemName } - 售后纠纷处理</title>
    <%@ include file="/WEB-INF/views/common/import.jsp"  %>
    <%@ include file="/WEB-INF/views/common/content.jsp"  %>
    
	<script type="text/javascript" src="${contextPath }/app/orders/saleDispute.js" ></script>
	
	<script type="text/javascript">
		
		Ext.onReady(function(){

			
			//需要判断用户是否拥有查看所有订单的权利，默认只显示自己添加的订单
			var grid=new caiban.easyadmin.orders.saledispute.SaleDisputeGrid({
				id:SALEDISPUTE.SALEDISPUTE_GRID,
				region:"center",
				layout:"fit",
				contextmenu:new Ext.menu.Menu({
						items:[{
							text: '处理情况',
							menu:[
							<s:iterator id="list" value="paramsList" status="idx">
							<s:if test="#idx.index > 0">,</s:if>
							{
								text:"${list.value}",
								handler:function(){
									caiban.easyadmin.orders.saledispute.AssignResolved(grid,"${list.key}");
								}
							}
							</s:iterator>
							]
						},{
							text:"指派给",
							menu:[{
								text:"无",
								handler:function(){
									
								}
							}]
						}]
					})
			});
			
			grid.listByResolvedParam();
			
			var viewport = new Ext.Viewport({
				layout:"border",
				items:[grid] //,searchForm
			});

			
		}); 
		
	</script>
    
  </head>
  
  <body>

  </body>
</html>
