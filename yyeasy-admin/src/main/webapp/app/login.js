Ext.namespace('caiban.easyadmin');

var LOGIN = new function(){
	this.LOGINWINDOW="loginwindow";
}

caiban.easyadmin.LoginForm=Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
		config = config || {};
		Ext.apply(this,config);
		var c={
			layout:'form',
			frame:true,
			labelAlign:'right',
			labelWidth:60,
			defaults:{
				anchor:"95%",
				xtype:"textfield"
			},
			items:[{
				fieldLabel:'用户名',
				name:'user.username',
				id:'username',
				allowBlank:false
			},{
				inputType:'password',
				fieldLabel:'密码',
				name:'user.password',
				id:'password',
				allowBlank:false
			}]
		};
		
		caiban.easyadmin.LoginForm.superclass.constructor.call(this,c);
		
	},
	initFocus:function(){
		this.findById("username").focus(true,100);
	}
});

/**
 * 用户登录动作
 * form:登录的表单对象
 * onSuccess:登录成功后要做的事
 * */
caiban.easyadmin.UserLogin = function(form,onSuccess){
	form.getForm().submit({
		url:Context.ROOT+"/checkuser.act",
		method:"post",
		type:"json",
		success:onSuccess,
		failure:function(_form,_action){
			Ext.MessageBox.show({
				title:MESSAGE.title,
				msg : _action.result.data,
				buttons:Ext.MessageBox.OK,
				icon:Ext.MessageBox.ERROR
			});
		}
	});
}

caiban.easyadmin.UserLoginWin = function(doSuccess){
	var form=new caiban.easyadmin.LoginForm({
		region:"center"
	});
	
	var win=new Ext.Window({
		id:LOGIN.LOGINWINDOW,
		layout:'border',
		iconCls:"item-key",
		width:300,
		height:150,
		closable:false,
		title:"EasyAdmin - 登录",
		modal:true,
		items:[form],
		keys:[{
			key:[10,13],
			fn:function(){
				caiban.easyadmin.UserLogin(form,doSuccess);
			}
		}],
		buttons:[{
			text:'登录',
			handler:function(btn){
				caiban.easyadmin.UserLogin(form,doSuccess);
			}
		}]
	});
	
	win.show();
	
	form.initFocus();
}

