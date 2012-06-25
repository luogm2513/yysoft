Ext.namespace("caiban.easyadmin.auth.role");

/**
 * 常量
 * */
var Const = new function(){
	this.RIGHT_WIN="rightinfowin";
	this.ROLE_GRID = "roleGrid";
	this.RIGHT_INROLE_GRID = "rightInRoleGrid";
	this.RIGHT_NOTINROLE_GRID = "rightNotInRoleGrid";
	this.USER_WIN="userinfowin";
	this.USER_INROLE_GRID="userInRoleGrid";
	this.USER_NOTINROLE_GRID="userNotInRoleGrid";
}

Ext.onReady(function(){
	var rightGrid = new caiban.easyadmin.auth.role.RightGrid({
		id:Const.RIGHT_INROLE_GRID,
		title:"与角色关联的权限",
		listUrl:Context.ROOT+"/auth/role!listRightInRole.act",
		mytoolbar:[{
			iconCls:"add16",
			text:"分配权限",
			scope:this,
			handler:function(){
				caiban.easyadmin.auth.role.AssignRight2RoleWin();
			}
		},{
			iconCls:"delete16",
			text:"删除关联",
			handler:function(){
				Ext.MessageBox.confirm(MESSAGE.title,MESSAGE.confirmDelete,function(btn){
					if(btn!="yes"){
						return ;
					}
					var roleGrid=Ext.getCmp(Const.ROLE_GRID);
					var selectedRecord = roleGrid.getSelectionModel().getSelected();
					
					var row = rightGrid.getSelectionModel().getSelections();
					var _ids = new Array();
					for (var i=0,len = row.length;i<len;i++){
						var _id=row[i].get("id");
						_ids.push(_id);
					}
					Ext.Ajax.request({
						url:Context.ROOT+"/auth/role!deleteRoleRight.act",
						params:{"role.id":selectedRecord.get("id"),"rightArray":_ids.join(",")},
						success:function(response,opt){
							var obj = Ext.decode(response.responseText);
							if(obj.success){
								rightGrid.getStore().reload();
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
	
	var role = new caiban.easyadmin.auth.role.RoleEditGrid({
		listeners :{
			"datachange":function(){
				alert(1)
			}
		}
	});
	
	var right = {region:'north',layout:"fit",height:300,items:[
		rightGrid
	]};
	
	var user = {region:'center',layout:'fit',items:[
		new caiban.easyadmin.auth.role.UserGrid({
			id:Const.USER_INROLE_GRID,
			title:"与角色关联的用户"
		})
	]};
	
	var viewport = new Ext.Viewport({
		layout:"border",
		items:[{layout:"fit",region:"west",width:350,items:[role]},
			{layout:"border",region:"center",items:[user,right]}]
//		items:[role]
	});
})

caiban.easyadmin.auth.role.RoleEditGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	constructor:function(config){
		config = config || {};
		Ext.apply(this,config);
		var _fields = this.roleRecord;
		var _url = this.storeUrl;
		var _store = new Ext.data.JsonStore({
			root:"records",
			totalProperty:'totals',
			remoteSort:false,
			fields:_fields,
			url:_url,
			autoLoad:true
		});
		
		var _sm=new Ext.grid.CheckboxSelectionModel({singleSelect:true});
		var _cm=new Ext.grid.ColumnModel([_sm,{
			id:"edit-id",
			header:'编号',
			width:10,
			sortable:true,
			dataIndex:'id',
			hidden:true
		},{
			id:"edit-name",
			header:'角色名',
			sortable:true,
			dataIndex:'name',
			editor:{
				xtype:'textfield',
				allowBlank:false
			}
		},{
			id:"edit-remark",
			header:'备注',
			sortable:true,
			dataIndex:'remark',
			editor:{
				xtype:"textarea"
			}
		}]);
		
		var c={
			id:Const.ROLE_GRID,
			iconCls:"icon-grid",
			viewConfig:{
				autoFill:true
			},
			store:_store,
			sm:_sm,
			cm:_cm,
			tbar:[{
				iconCls:"add16",
				text:"新增",
				scope:this,
				handler:function(){
					var r=new this.roleRecord({
						id:0,
						name:"",
						remark:""
					});
					this.stopEditing();
					_store.insert(_store.getCount(),r);
					this.startEditing(_store.getCount()-1,2);
				}
			},{
				iconCls:"delete16",
				text:"删除角色",
				scope:this,
				handler:function(){
					this.deleteRole();
				}
			},{
				iconCls:"refresh16",
				text:"刷新",
				handler:function(){
					_store.reload();
				}
			}]
		};
		
		caiban.easyadmin.auth.role.RoleEditGrid.superclass.constructor.call(this,c);
		
		this.on("afteredit",function(e){
			var _url;
			if(e.record.get("id")>0){
				_url=Context.ROOT+"/auth/role!updateRole.act";
			}else{
				_url=Context.ROOT+"/auth/role!createRole.act";
			}
			this.saveRole(e.record,_url);
		});
		
		this.on("rowclick",function(grid,rowIndex,e){
			var id=grid.getSelectionModel().getSelected().get("id");
			Ext.getCmp(Const.RIGHT_INROLE_GRID).getStore().baseParams ={"role.id":id};
			Ext.getCmp(Const.RIGHT_INROLE_GRID).getStore().load();
			Ext.getCmp(Const.USER_INROLE_GRID).getStore().baseParams ={"role.id":id};
			Ext.getCmp(Const.USER_INROLE_GRID).getStore().load();
		});
		
	},
	storeUrl:Context.ROOT+"/auth/role!listRole.act",
	roleRecord:Ext.data.Record.create([{
		name: 'id',
		type: 'int'
	},{
		name: 'name',
		type: 'string'
	},{
		name: 'remark',
		type: 'string'
	}]),
	saveRole:function(record,_url){
		Ext.Ajax.request({
			url:_url,
			params:{
				"role.id":record.get("id"),
				"role.name":record.get("name"),
				"role.remark":record.get("remark")
			},
			scope:this,
			success:function(response,opt){
				var obj = Ext.decode(response.responseText);
				if(obj.success){
					this.getStore().reload();
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
	},
	deleteRole:function(){
//		Ext.MessageBox.confirm(MESSAGE.title,MESSAGE.confirmDelete,this.deleteRole);
		
		var record = this.getSelectionModel().getSelected();
		Ext.Ajax.request({
			url:Context.ROOT+"/auth/role!deleteRole.act",
			params:{
				"role.id":record.get("id")
			},
			scope:this,
			success:function(response,opt){
				var obj = Ext.decode(response.responseText);
				if(obj.success){
					this.getStore().remove(record);
					caiban.utils.Msg(MESSAGE.picTitleInfo,MESSAGE.deleteSuccess);
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
	}
});

caiban.easyadmin.auth.role.RightGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		var _fields = this.rightRecord;
		var _url = this.listUrl; 
		var _store = new Ext.data.JsonStore({
			root:"records",
			totalProperty:'totals',
			remoteSort:false,
			fields:_fields,
			url:_url,
			autoLoad:false
		});
		
		var _sm=new Ext.grid.CheckboxSelectionModel();
		var _cm=new Ext.grid.ColumnModel([_sm,{
			header:'编号',
			width:10,
			sortable:true,
			dataIndex:'id',
			hidden:true
		},{
			header:'权限名',
			sortable:true,
			dataIndex:'name'
		},{
			header:'规则',
			sortable:true,
			dataIndex:'content'
		},{
			header:'菜单',
			sortable:true,
			dataIndex:'menu',
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				if(value!=null && value!=""){
					return '<a href="'+Context.ROOT+record.get("menuUrl")+'" >'+value+'</a>';
				}
			}
		}]);
		
		var c={
			iconCls:"icon-grid",
			viewConfig:{
				autoFill:true
			},
			store:_store,
			sm:_sm,
			cm:_cm,
			tbar:this.mytoolbar
		};
		
		caiban.easyadmin.auth.role.RightGrid.superclass.constructor.call(this,c);
	},
	mytoolbar:[{}],
	rightRecord:Ext.data.Record.create([{
		name: 'id',
		type: 'int'
	},{
		name: 'parentId',
		type: 'int'
	},{
		name: 'name',
		type: 'string'
	},{
		name: 'content',
		type: 'string'
	},{
		name: 'l',
		type: 'int'
	},{
		name: 'r',
		type: 'int'
	},{
		name: 'menu',
		type: 'string'
	},{
		name: 'menuUrl',
		type: 'string'
	},{
		name: 'menuCss',
		type: 'string'
	},{
		name: 'gmtCreated',
		type: 'date'
	}]),
	listUrl:Context.ROOT+"/auth/role!listRightInRole.act"
});

caiban.easyadmin.auth.role.AssignRight2RoleWin = function(){
	var roleGrid=Ext.getCmp(Const.ROLE_GRID);
	var selectedRecord = roleGrid.getSelectionModel().getSelected();
	if(typeof(selectedRecord) == "undefined"){
		return ;
	}
	
	var rightGrid=Ext.getCmp(Const.RIGHT_INROLE_GRID);
	
	var grid = new caiban.easyadmin.auth.role.RightGrid({
		id:Const.RIGHT_NOTINROLE_GRID,
//		region:"center",
//		layout:"fit",
		listUrl:Context.ROOT+"/auth/role!listRightNotInRole.act",
		mytoolbar:[{
			iconCls:"add16",
			text:"关联",
			scope:this,
			handler:function(){
				var row = grid.getSelectionModel().getSelections();
				var _ids = new Array();
				for (var i=0,len = row.length;i<len;i++){
					var _id=row[i].get("id");
					_ids.push(_id);
				}
				Ext.Ajax.request({
					url:Context.ROOT+"/auth/role!createRoleRight.act",
					params:{"role.id":selectedRecord.get("id"),"rightArray":_ids.join(",")},
					success:function(response,opt){
						var obj = Ext.decode(response.responseText);
						if(obj.success){
							grid.getStore().reload();
							rightGrid.getStore().reload();
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
			}
		}]
	})
	var win = new Ext.Window({
		id:Const.RIGHT_WIN,
		title:"未分配的权限信息",
		width:500,
		height:360,
		modal:true,
		layout:"fit",
		items:[grid],
		buttons:[{
			text:"close",
			handler:function(){
				win.close();
			}
		}]
	});
	win.show();
	grid.getStore().baseParams = {"role.id":selectedRecord.get("id")};
	grid.getStore().load();
}

caiban.easyadmin.auth.role.AssignUser2RoleWin = function(){
	var roleGrid=Ext.getCmp(Const.ROLE_GRID);
	var selectedRecord = roleGrid.getSelectionModel().getSelected();
	if(typeof(selectedRecord) == "undefined"){
		return ;
	}
	
	var grid = new caiban.easyadmin.auth.role.UserGrid({
		id:Const.USER_NOTINROLE_GRID,
		listUrl:Context.ROOT+"/auth/role!listUserNotInRole.act",
		mytoolbar:[{
			iconCls:"add16",
			text:"关联",
			scope:this,
			handler:function(){
				// TODO 关联用户到角色信息
				var row = grid.getSelectionModel().getSelections();
				var _ids = new Array();
				for (var i=0,len = row.length;i<len;i++){
					var _id=row[i].get("id");
					_ids.push(_id);
				}
				Ext.Ajax.request({
					url:Context.ROOT+"/auth/role!createUserRole.act",
					params:{"role.id":selectedRecord.get("id"),"userArray":_ids.join(",")},
					success:function(response,opt){
						var obj = Ext.decode(response.responseText);
						if(obj.success){
							grid.getStore().reload();
							Ext.getCmp(Const.USER_INROLE_GRID).getStore().reload();
//							rightGrid.getStore().reload();
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
			}
		}]
	});
	
	var win = new Ext.Window({
		id:Const.USER_WIN,
		title:"用户信息",
		width:500,
		height:360,
		modal:true,
		layout:"fit",
		items:[grid]
	});
	win.show();
	grid.getStore().baseParams = {"role.id":selectedRecord.get("id")};
	grid.getStore().load();
}

caiban.easyadmin.auth.role.UserGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		var _fields = this.userRecord;
		var _url = this.listUrl; 
		var _store = new Ext.data.JsonStore({
			root:"records",
			totalProperty:'totals',
			remoteSort:false,
			fields:_fields,
			url:_url,
			autoLoad:false
		});
		
		var _sm=new Ext.grid.CheckboxSelectionModel();
		var _cm=new Ext.grid.ColumnModel([_sm,{
			header:'编号',
			width:10,
			sortable:true,
			dataIndex:'id',
			hidden:true
		},{
			header:'用户名',
			sortable:true,
			dataIndex:'username'
		},{
			header:'Email',
			sortable:false,
			dataIndex:'email'
		},{
			header:'最后登录时间',
			sortable:false,
			dataIndex:'gmtLogin',
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				return "time";
			}
		},{
			header:'创建时间',
			sortable:true,
			dataIndex:'gmtCreate',
			hidden:true,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				return "time";
			}
		}]);
		
		var c={
			iconCls:"icon-grid",
			viewConfig:{
				autoFill:true
			},
			store:_store,
			sm:_sm,
			cm:_cm,
			tbar:this.mytoolbar,
			bbar: new Ext.PagingToolbar({
				pageSize : Context.PAGE_SIZE,
				store : _store,
				displayInfo: true,
				displayMsg: MESSAGE.paging.displayMsg,
				emptyMsg : MESSAGE.paging.emptyMsg,
				beforePageText : MESSAGE.paging.beforePageText,
				afterPageText : MESSAGE.paging.afterPageText,
				paramNames : MESSAGE.paging.paramNames
			})
		};
		
		caiban.easyadmin.auth.role.UserGrid.superclass.constructor.call(this,c);
	},
	mytoolbar:[{
		iconCls:"adduser16",
		text:"添加用户",
		scope:this,
		handler:function(){
			caiban.easyadmin.auth.role.AssignUser2RoleWin();
		}
	},{
		iconCls:"delete16",
		text:"删除关联",
		scope:this,
		handler:function(){
			Ext.MessageBox.confirm(MESSAGE.title,MESSAGE.confirmDelete,function(btn){
					if(btn!="yes"){
						return ;
					}
					var roleGrid=Ext.getCmp(Const.ROLE_GRID);
					var selectedRecord = roleGrid.getSelectionModel().getSelected();
					
					var userGrid = Ext.getCmp(Const.USER_INROLE_GRID);
					var row = userGrid.getSelectionModel().getSelections();
					var _ids = new Array();
					for (var i=0,len = row.length;i<len;i++){
						var _id=row[i].get("id");
						_ids.push(_id);
					}
					Ext.Ajax.request({
						url:Context.ROOT+"/auth/role!deleteUserRole.act",
						params:{"role.id":selectedRecord.get("id"),"userArray":_ids.join(",")},
						success:function(response,opt){
							var obj = Ext.decode(response.responseText);
							if(obj.success){
//								rightGrid.getStore().reload();
								userGrid.getStore().reload();
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
	}],
	deleteUserRole:function(btn){
		if(btn!="yes"){
			return ;
		}
		// TODO 删除用户角色关联
	},
	userRecord:Ext.data.Record.create([{
		name: 'id',
		type: 'int'
	},{
		name: 'username',
		type: 'string'
	},{
		name: 'password',
		type: 'string'
	},{
		name: 'gmtLogin',
		type: 'date'
	},{
		name: 'gmtCreated',
		type: 'date'
	}]),
	listUrl:Context.ROOT+"/auth/role!listUserInRole.act"
});