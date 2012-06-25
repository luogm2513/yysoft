Ext.namespace("caiban.easyadmin.auth.right");

/**
 * 权限管理中会用到的变量
 * */
var Const = new function(){
	this.RIGHT_WIN="rightinfowin";
	this.RIGHT_TREE = "rightTree";
	this.CONTEXTMENU = "menu-add-right";
}

Ext.onReady(function(){
//	alert(getMsg("here is {0} {1} {1} {0}",["中文","非中文"]))
	
	var contextmenu=new Ext.menu.Menu({
		id:Const.CONTEXTMENU,
		items:[{
			text:'增加权限',
			cls:'add16',
			handler:function(menu){
				caiban.easyadmin.auth.right.AddFormWin();
			}
		},{
			text:'修改权限',
			cls:'edit16',
			handler:function(menu){
				caiban.easyadmin.auth.right.EditFormWin();
			}
		},{
			text:'删除权限',
			cls:'delete16',
			handler:function(menu){
				var tree = Ext.getCmp(Const.RIGHT_TREE);
				var node = tree.getSelectionModel().getSelectedNode();
//				alert(tree.getRootNode() == node)
				if(tree.getRootNode() == node){
					return ;
				}
				Ext.MessageBox.confirm(MESSAGE.title,MESSAGE.confirmDelete, function(btn){
					if(btn != "yes"){
						return false;
					}
					//从服务端删除权限节点
					Ext.Ajax.request({
						url:Context.ROOT+"/auth/right!deleteRight.act",
						params:{"right.id":node.attributes.data},
						success:function(response,opt){
							var obj = Ext.decode(response.responseText);
							if(obj.success){
								node.remove();
							}else{
								Ext.MessageBox.show({
									title:MESSAGE.title,
									msg : MESSAGE.saveFailure,
									buttons:Ext.MessageBox.OK,
									icon:Ext.MessageBox.ERROR
								});
							}
						},
						failure:function(response,opt){
							Ext.MessageBox.show({
								title:MESSAGE.title,
								msg : MESSAGE.submitFailure,
								buttons:Ext.MessageBox.OK,
								icon:Ext.MessageBox.ERROR
							});
						}
					});
				});
			}
		}]
	});
	
	var tree=caiban.easyadmin.auth.right.RightTree({
		id:Const.RIGHT_TREE,
		region:"west",
		contextmenu:contextmenu
	});
	
	var rightDetail =caiban.easyadmin.auth.right.RightView(); 
	
	var viewport=new Ext.Viewport({
		layout:"border",
		items:[tree,rightDetail]
	});
	
});

caiban.easyadmin.auth.right.RightView = function(){
	return {region:"center",title:"right detail",html:"here is right details"};
}

caiban.easyadmin.auth.right.AddFormWin = function(){
	var form = new caiban.easyadmin.auth.right.InfoForm();
	form.saveUrl = Context.ROOT+"/auth/right!createRight.act";
	form.initParentForAdd();
	
	var win = new Ext.Window({
		id:Const.RIGHT_WIN,
		title:"添加权限信息",
		width:380,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	win.show();
}

caiban.easyadmin.auth.right.EditFormWin = function(){
	var tree = Ext.getCmp(Const.RIGHT_TREE);
	var node = tree.getSelectionModel().getSelectedNode();
	if(tree.getRootNode() == node){
		return false;
	}
	var form = new caiban.easyadmin.auth.right.InfoForm();
	form.saveUrl = Context.ROOT+"/auth/right!updateRight.act";
	form.initParentForUpdate();
	form.loadRight();
	var win = new Ext.Window({
		id:Const.RIGHT_WIN,
		title:"修改权限信息",
		width:380,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	win.show();
}
caiban.easyadmin.auth.right.InfoForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
		config = config || {};
		Ext.apply(this,config);
		var c = {
			labelAlign : "right",
			labelWidth : 80,
			region:"center",
			layout:"form",
			bodyStyle:'padding:5px 0 0',
			frame:true,
			defaults:{
				anchor:"95%",
				xtype:"textfield",
				labelSeparator:""
			},
			items:[{
				xtype:"hidden",
				id:"id",
				name:"right.id"
			},{
				xtype:"hidden",
				id:"parentId",
				name:"right.parentId"
			},{
				xtype:"textfield",
				id:"parent",
				readOnly:true,
				fieldLabel:"上级权限"
			},{
				xtype:"textfield",
				id:"name",
				name:"right.name",
				fieldLabel:"权限名",
				itemCls:"required",
				allowBlank:false
			},{
				xtype:"textarea",
				id:"content",
				name:"right.content",
				fieldLabel:"规则"
			},{
				xtype:"textfield",
				id:"menu",
				name:"right.menu",
				fieldLabel:"菜单名"
			},{
				xtype:"textfield",
				id:"menuUrl",
				name:"right.menuUrl",
				fieldLabel:"菜单连接"
			},{
				xtype:"textfield",
				id:"menuCss",
				name:"right.menuCss",
				fieldLabel:"菜单样式"
			}
			],
			buttons:[{
				text:"保存",
				handler:this.saveForm,
				scope:this
			},{
				text:"关闭",
				handler:function(){
					Ext.getCmp(Const.RIGHT_WIN).close();
				}
			}]
		};
		caiban.easyadmin.auth.right.InfoForm.superclass.constructor.call(this,c);
	},
	initParentForAdd:function(){
		var tree = Ext.getCmp(Const.RIGHT_TREE);
		var node = tree.getSelectionModel().getSelectedNode();
//		alert(node.attributes.data)
		this.findById("parentId").setValue(node.attributes.data);
		this.findById("parent").setValue(node.text);
	},
	initParentForUpdate:function(){
		var tree = Ext.getCmp(Const.RIGHT_TREE);
		var node = tree.getSelectionModel().getSelectedNode();
		this.findById("parent").setValue(node.parentNode.text);
	},
	saveUrl:Context.ROOT+"/auth/right!createRight.act",
	saveForm:function(btn){
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
		var tree = Ext.getCmp(Const.RIGHT_TREE);
		var node = tree.getSelectionModel().getSelectedNode();
		if(this.findById("id").getValue() > 0){
			node.setText(this.findById("name").getValue()); 
		}else{
			node.leaf= false;
			tree.getLoader().load(node,function(){
				node.expand();
			});
		}
		Ext.getCmp(Const.RIGHT_WIN).close();
	},
	onSaveFailure:function(_form,_action){
		Ext.MessageBox.show({
			title:MESSAGE.title,
			msg : MESSAGE.saveFailure,
			buttons:Ext.MessageBox.OK,
			icon:Ext.MessageBox.ERROR
		});
	},
	loadRight:function(){
		var tree = Ext.getCmp(Const.RIGHT_TREE);
		var node = tree.getSelectionModel().getSelectedNode();
		
		var _fields = [{
			name : "right.id",
			mapping : "id"
		}, {
			name : "right.name",
			mapping : "name"
		}, {
			name : "right.parentId",
			mapping : "parentId"
		}, {
			name : "right.content",
			mapping : "content"
		}, {
			name : "right.menu",
			mapping : "menu"
		}, {
			name : "right.menuUrl",
			mapping : "menuUrl"
		}, {
			name : "right.menuCss",
			mapping : "memuCss"
		}];
		var form = this;
		var _store = new Ext.data.JsonStore({
			root : "records",
			fields : _fields,
			url : Context.ROOT + "/auth/right!listOneRight.act?right.id="+node.attributes.data,
			autoLoad : true,
			listeners : {
				"datachanged" : function() {
					var record = _store.getAt(0);
					if (record == null) {
						Ext.MessageBox.alert(MESSAGE.title,MESSAGE.loadError);
						Ext.getCmp(Const.RIGHT_WIN).close();
					} else {
						form.getForm().loadRecord(record);
					}
				}
			}
		});
		
	}
	
}); 
