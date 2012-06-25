<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'under';
	Ext.BLANK_IMAGE_URL = "${contextPath}/js/ext31/images/default/s.gif";
//	Ext.ERROR_INTERNET_INVALID_URL = "http://yy.caiban.net:880/errror.html";
	
	var Context = new function(){ 
		this.ROOT = "${contextPath}";
		this.ROOT_IMG = "${imgserver}";
		this.PAGE_SIZE = 20;
		this.LOGIN_USER="${loginUser.user.username}";
	}
	
	var MESSAGE = new function(){
		this.title =  "信息提示";
		this.picTitleInfo = "<img src='${contextPath}/themes/boomy/infoabout24.png' />"; 
		this.unValidate = "表单填写有错误，请仔细检查一下表单 :)";
		this.saveFailure = "表单没有被保存<br /><br />请多尝试几次，如果还有问题请 <a href='mailto:mays@caiban.net' >联系我们</a>";
		this.loadError = "数据没有被载入<br /><br />请多尝试几次，如果还有问题请 <a href='mailto:mays@caiban.net' >联系我们</a>";
		this.confirmDelete = "确定要删除吗?";
		this.submitFailure = "请求没有被正确处理,请检查网络是否连接正常!";
		this.saveSuccess = "保存成功";
		this.noRecord = "没有记录";
		this.needOneRecord="请选择一条记录";
		this.noAuth = "你没有权限这么做,如需帮助,请<a href='mailto:mays@caiban.net' >联系我们</a>";
		this.ajaxError = "噢！发生了一点错误，请再试一次<br/>如果还是这样，请 <a href='mailto:mays@caiban.net' >联系我们</a>";
		this.paging = {
			displayMsg:'显示第 {0} - {1} 第记录,共 {2} 条',
			emptyMsg:'没有可显示的记录',
			beforePageText:'第',
			afterPageText:'页,共{0}页',
			paramNames:{start:'page.start', limit:'page.limit', sort:'page.sort', dir:'page.dir'}
		};
		this.saving = "正在保存...";
		this.deleting = "正在删除...";
		this.loading = "正在载入...";
		this.uploading = "正在上传...";
		this.uploadFailure = "文件没有被上传：{0}";
		this.deleteSuccess = "选定记录已被删除";
	}
	
	function getMsg(msg,value){
		var s = "";
		for(var i=0, l=value.length;i<l;i++){
			s=new RegExp("\\{"+i+"\\}","g");
			msg = msg.replace(s,value[i]);
		}
		return msg;
	}

	caiban.utils = function(){
		var msgCt;

	    function createBox(t, s){
	        return ['<div class="msg">',
	                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
	                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
	                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
	                '</div>'].join('');
	    }
	    
	   	return {
	       Msg : function(title, format){
	       	if(!msgCt){
	                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
	            }
	           msgCt.alignTo(document, 't-t');
	           var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
	           var m = Ext.DomHelper.append(msgCt, {html:createBox(title, s)}, true);
	           m.slideIn('t').pause(1).ghost("t", {remove:true});
	   		}
       	};
	}();
	
	//AJAX事件
	Ext.Ajax.on('requestcomplete',function(conn,response,options){
		if(options.isUpload){
			return true;
		}

		if(typeof(response.getResponseHeader("sessionstatus"))!= 'undefined'){
			//TODO 用户没有登录
			caiban.easyadmin.UserLoginWin(function(form,action){
				Ext.getCmp(LOGIN.LOGINWINDOW).close();
			});
		}
	}, this);

	Ext.Ajax.on('requestexception',function(conn,response,options){
        //response.responseText
		if("${debug}"=="true"){
			var win = new Ext.Window({
				iconCls:"infoabout16",
				title:MESSAGE.title,
				width:700,
				autoHeight:true,
				modal:true,
				items:[{
					xtype:"panel",
					region:"center",
					layout:"fit",
					html:response.responseText,
					width:"100%",
					height:500,
					autoScroll:true
				}],
				bbar:["->",{
					iconCls:"mailsend16",
					text:"发送错误报告",
					handler:function(btn){
						caiban.utils.Msg(MESSAGE.title,"报告已发送！～");
					}
				}]
			});
			win.show();
			
		}else{
			Ext.MessageBox.show({
				title:Context.MSG_TITLE,
				msg : "#"+response.status+"<br/>"+MESSAGE.ajaxError,
	          	buttons:Ext.MessageBox.OK,
	          icon:Ext.MessageBox.ERROR
	       	});
		}
	},this);
	
</script>

<!-- google analytic -->
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-8963667-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
