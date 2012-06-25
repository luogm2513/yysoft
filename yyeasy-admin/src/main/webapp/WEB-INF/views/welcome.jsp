<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <title>${systemName } - easy admin your business</title>
    <%@ include file="/WEB-INF/views/common/import.jsp"  %>
    <%@ include file="/WEB-INF/views/common/content.jsp"  %>
    
    <script type="text/javascript" src="${contextPath }/js/extux/Portal.js" ></script>
    <script type="text/javascript" src="${contextPath }/js/extux/PortalColumn.js" ></script>
    <script type="text/javascript" src="${contextPath }/js/extux/Portlet.js" ></script>
	<script type="text/javascript" src="${contextPath }/app/welcome.js" ></script>
    <script type="text/javascript" >
    Ext.onReady(function(){
    	var tools = [{
            id:'close',
            handler: function(e, target, panel){
                panel.ownerCt.remove(panel, true);
            }
        }];
        
    	var viewport = new Ext.Viewport({
        	autoScroll:true,
        	items:[{
				xtype:"portal",
              margins:'35 5 5 0',
              border:false,
              items:[{
                	columnWidth:.5,
                	style:'padding:10px 0 10px 10px',
                	items:[{
                		title: '待处理的纠纷',
                		tools:tools,
                		html:"没有纠纷"
                    },{
                		title: '纠纷统计',
                		tools:tools,
                		html:"没有数据"
                    }]
                },{
                	columnWidth:.5,
                	style:'padding:10px',
                	items:[{
                		title: '待处理的订单',
                		tools:tools,
                		html:"没有订单"
                    },{
                		title: '订单统计',
                		tools:tools,
                		html:"没有数据"
                    }]
                }]
            }]
        });
   	})
    </script>
  </head>
  
  <body>
  </body>
</html>
