Ext.namespace("caiban.easyadmin");

Ext.onReady(function(){
	var west=new Ext.tree.TreePanel({
        id:'forum-tree',
        region:'west',
        title:'系统导航',
        split:true,
        width: 220,
        minSize: 175,
        maxSize: 400,
        collapsible: true,
        margins:'2,0,2,2',
        cmargins:'2,2,2,2',
        collapseMode:'mini',
        dataUrl:Context.ROOT+'/mymenu.act',
        rootVisible:false,
        lines:false,
        autoScroll:true,
        root: new Ext.tree.AsyncTreeNode({
          	text: '导航',
          	expanded:true
      	})
    });
    
    var center=new Ext.TabPanel({
    	region:'center',
        margins:'0 5 5 0',
        resizeTabs: true,
        minTabWidth: 100,
        tabWidth: 100,
        enableTabScroll: true,
        activeTab: 0,
        items:[{
        	id:'welcome-panel',
        	iconCls:"home16",
        	title: '我的桌面',
        	closable: true,
        	autoScroll:true,
         	layout : 'fit',
         	html:'<iframe src="' + Context.ROOT+ '/welcome.act" frameBorder=0 scrolling = "auto" style = "width:100%;height:100%"></iframe>'
        }]
    });
	
	var north={
		region:'north',
		height:0,
		bbar:['->',{ //new Ext.ux.ThemeChange(),
			iconCls:'userid16',
			text:'<b>'+Context.LOGIN_USER+'</b>',
			handler:function(btn){
//				caiban.utils.Msg(MESSAGE.title, "个人信息暂不能被查看和修改！");
			}
		},{
			text:'修改密码',
			iconCls:'key16',
			handler:function(btn){
				caiban.easyadmin.ChangePasswordWin();
			}
		},{
			text:'退出',
			iconCls:'close16',
			handler:function(btn){
				window.location.href = Context.ROOT+"/logout.act";
			}
		}],
		html:''
	};
	
	var viewport=new Ext.Viewport({
		layout:'border',
		items:[west,center,north]
	});
	
	west.on('click',function(node, e){
		if(node.isLeaf()){
			e.stopEvent();
			var id = 'contents-' + node.attributes.data;
	        var tab = this.getComponent(id);
	        if(tab){
	            center.setActiveTab(tab);
	        }else{
	            var p = center.add(new Ext.Panel({
	                id: id,
	                title:node.text,
	                closable: true,
	                autoScroll:true,
	                layout : 'fit', 
					html : '<iframe src="' + Context.ROOT+ node.attributes.menuUrl + '" frameBorder=0 scrolling = "auto" style = "width:100%;height:100%"></iframe> '
	            }));
	            center.setActiveTab(p);
	        }
		}
	});
	
	west.getLoader().on("beforeload",function(treeLoader,node){
		this.baseParams.parentRightId= node.attributes.data;  //根据data返回的JSON数据获取
	},west.getLoader());
});

/**
 * 密码修改窗口
 * */
caiban.easyadmin.ChangePasswordWin = function(){
	var form = new caiban.easyadmin.PasswordForm({
		region:"center"
	});
	
	var win = new Ext.Window({
		id:"changepasswordwin",
		title:"修改密码",
		iconCls:"key16",
		width:300,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	win.show();
}

/**
 * 密码修改表单
 * */
caiban.easyadmin.PasswordForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var c={
			labelAlign : "right",
			labelWidth : 80,
			region:"center",
			layout:"form",
			bodyStyle:'padding:5px 0 0',
			frame:true,
			defaults:{
				anchor:"90%",
				xtype:"textfield",
				labelSeparator:""
			},
			items:[{
				xtype:"textfield",
				inputType:"password",
				id:"originalPassword",
				name:"originalPassword",
				fieldLabel:"旧密码",
				itemCls:"required",
				allowBlank:false
			},{
				xtype:"textfield",
				inputType:"password",
				id:"newPassword",
				name:"newPassword",
				fieldLabel:"新密码",
				itemCls:"required",
				allowBlank:false
			},{
				xtype:"textfield",
				inputType:"password",
				id:"verifyPassword",
				name:"verifyPassword",
				fieldLabel:"确认密码",
				itemCls:"required",
				allowBlank:false
			}],
			buttons:[{
				text:"保存",
				scope:this,
				handler:this.saveForm
			},{
				text:"关闭",
				handler:function(){
					Ext.getCmp("changepasswordwin").close();
				}
			}]
		};
		
		caiban.easyadmin.PasswordForm.superclass.constructor.call(this,c);
	},
	saveUrl:Context.ROOT+"/changePassword.act",
	saveForm:function(){
		if(this.getForm().isValid()){
			this.getForm().submit({
				url:this.saveUrl,
				method:"post",
				type:"json",
				success:this.onSaveSuccess,
				failure:this.onSaveFailure,
				scope:this
			});
		}else{
			Ext.MessageBox.show({
				title:MESSAGE.title,
				msg : MESSAGE.unValidate,
				buttons:Ext.MessageBox.OK,
				icon:Ext.MessageBox.ERROR
			});
		}
	},
	onSaveSuccess:function(_form,_action){
		Ext.MessageBox.show({
			title:MESSAGE.title,
			msg : MESSAGE.saveSuccess,
			buttons:Ext.MessageBox.OK,
			icon:Ext.MessageBox.INFO,
			fn:function(buttonId,text,opt){
				Ext.getCmp("changepasswordwin").close();
			}
		});
	},
	onSaveFailure:function(_form,_action){
		Ext.MessageBox.show({
			title:MESSAGE.title,
			msg : MESSAGE.saveFailure,
			buttons:Ext.MessageBox.OK,
			icon:Ext.MessageBox.ERROR
		});
	}
});