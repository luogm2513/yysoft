<%@ page language="java"  pageEncoding="UTF-8"%>
<script type="text/javascript" >
	var MESSAGE = new function(){
		this.title =  "信息提示";
		this.unValidate = "表单填写有错误，请仔细检查一下表单 :)";
		this.saveFailure = "表单没有被保存<br /><br />请多尝试几次，如果还有问题请<a href='mailto:mays@caiban.net' >联系我们</a>";
		this.loadError = "数据没有被载入<br /><br />请多尝试几次，如果还有问题请<a href='mailto:mays@caiban.net' >联系我们</a>";
		this.confirmDelete = "确定要删除吗?";
		this.submitFailure = "请求没有被正确处理,请检查网络是否连接正常!";
		this.saveSuccess = "保存成功";
		this.noRecord = "没有记录";
		this.needOneRecord="请选择一条记录";
		this.noAuth = "你没有权限这么做,如需帮助,请<a href='mailto:mays@caiban.net' >联系我们</a>"
		this.paging = {
			displayMsg:'显示第 {0} - {1} 第记录,共 {2} 条',
			emptyMsg:'没有可显示的记录',
			beforePageText:'第',
			afterPageText:'页,共{0}页',
			paramNames:{start:'page.start', limit:'page.limit'}
		}
	}
	
	function getMsg(msg,value){
		var s = "";
		for(var i=0, l=value.length;i<l;i++){
			s=new RegExp("\\{"+i+"\\}","g");
			msg = msg.replace(s,value[i]);
		}
		return msg;
	}
</script>
