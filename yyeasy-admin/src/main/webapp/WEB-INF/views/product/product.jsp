<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <title>${systemName } - 产品信息管理</title>
    <%@ include file="/WEB-INF/views/common/import.jsp"  %>
    <%@ include file="/WEB-INF/views/common/content.jsp"  %>
    
	<script type="text/javascript" src="${contextPath }/js/extux/ComboBoxTree.js" ></script>
	<script type="text/javascript" src="${contextPath }/js/extux/FileUploadField.js" ></script>
	<script type="text/javascript" src="${contextPath }/app/lib/UploadWin.js" ></script>
	<script type="text/javascript" src="${contextPath }/app/product/productCategory.js" ></script>
	<script type="text/javascript" src="${contextPath }/app/product/productImage.js" ></script>
	<script type="text/javascript" src="${contextPath }/app/product/productStock.js" ></script>
	<script type="text/javascript" src="${contextPath }/app/product/product.js" ></script>
    
  </head>
  
  <body>
	<input type="hidden" id="authuserid" value="${product.uid }" />
  </body>
</html>
