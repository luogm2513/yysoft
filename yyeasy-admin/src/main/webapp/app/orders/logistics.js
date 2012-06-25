Ext.namespace("caiban.easyadmin.orders.logistics");

/**
 * 权限管理中会用到的变量
 * */
var Const = new function(){
	this.LOGISTICS_WIN="logisticswin";
	this.LOGISTICS_GRID = "logisticsgrid";
}

Ext.onReady(function(){
	
	var grid=new caiban.easyadmin.orders.logistics.LogisticsGrid({
		id:Const.LOGISTICS_GRID,
		region:"center",
		layout:"fit"
	});
	
	var viewport = new Ext.Viewport({
		layout:"border",
		items:[grid]
	});
}); 

caiban.easyadmin.orders.logistics.LogisticsGrid = Ext.extend(Ext.grid.GridPanel, {
	queryUrl:Context.ROOT+"/orders/logistics!listLogistics.act",
	constructor:function(config){
		config = config || {};
		Ext.apply(this,config);
		
		var _fields = this.fieldsRecord;
		var _url = this.queryUrl;
		var _store = new Ext.data.JsonStore({
			root:"records",
			totalProperty:"totals",
			remoteSort:true,
			fields:_fields,
			url:_url,
			autoLoad:true
		});
		
		var _sm=new Ext.grid.CheckboxSelectionModel();
		var _cm=new Ext.grid.ColumnModel([_sm,{
			header:"编号",
			width:10,
			sortable:true,
			dataIndex:"id",
			hidden:true
		},{
			header:"物流名称",
			dataIndex:"name"
		},{
			header:"联系电话",
			dataIndex:"phone"
		},{
			header:"物流地址",
			dataIndex:"address"
		},{
			header:"运输线路",
			dataIndex:"routes"
		},{
			header:"到货区",
			dataIndex:"arrivalArea"
		},{
			header:"备注",
			dataIndex:"remark",
			hidden:true
		},{
			header:"发布人",
			dataIndex:"uid",
			hidden:true
		},{
			header:"发布时间",
			sortable:true,
			dataIndex:"gmtcreate",
			hidden:true,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				if(value!=null && value!=""){
					return value;
				}
			}
		}]);
		
		var c={
			viewConfig:{
				autoFill:true
			},
			store:_store,
			sm:_sm,
			cm:_cm,
			tbar:[{
				iconCls:"add16",
				text:"新增",
				handler:function(btn){
					caiban.easyadmin.orders.logistics.CreateLogisticsWin();
				}
			},{
				iconCls:"edit16",
				text:"编辑",
				handler:function(btn){
					// TODO 编辑物流信息
					var selectedRecord = Ext.getCmp(Const.LOGISTICS_GRID).getSelectionModel().getSelected();
					if(typeof(selectedRecord)=="undefined"){
						Ext.MessageBox.show({
							title:MESSAGE.title,
							msg : MESSAGE.needOneRecord,
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.WARNING
						});
						return false;
					}
					caiban.easyadmin.orders.logistics.UpdateLogisticsWin(selectedRecord.get("id"));
				}
			},{
				iconCls:"delete16",
				text:"删除",
				handler:function(btn){
					var grid = Ext.getCmp(Const.LOGISTICS_GRID);
					var sm=grid.getSelectionModel();
					
					if (sm.getCount() == 0){
						Ext.MessageBox.show({
							title:MESSAGE.title,
							msg : MESSAGE.needOneRecord,
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.WARNING
						});
					} else{
						Ext.MessageBox.confirm(MESSAGE.title, MESSAGE.confirmDelete, function(btn){
							if(btn != "yes"){
								return false;
							}
							
							var row = sm.getSelections();
							var _ids = new Array();
							for (var i=0,len = row.length;i<len;i++){
								var _id=row[i].get("id");
								_ids.push(_id);
							}
							
							//删除物流信息
							Ext.Ajax.request({
								url:Context.ROOT+"/orders/logistics!deleteLogistics.act",
								params:{"logisticsArray":_ids.join(",")},
								success:function(response,opt){
									var obj = Ext.decode(response.responseText);
									if(obj.success){
										grid.getStore().reload();
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
				}
			},"->",{
				iconCls:"view",
				text:"物流信息查询",
				handler:function(btn){
					window.open(Context.ROOT+"/s/logistics.act?logistics.eid="+_ESHOPID);
				}
			}],
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
		
		caiban.easyadmin.orders.logistics.LogisticsGrid.superclass.constructor.call(this,c);
	},
	fieldsRecord:["id","name","phone","address","routes","arrivalArea","remark","eid","uid"]
});

caiban.easyadmin.orders.logistics.LogisticsForm =Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var c={
			labelAlign : "right",
			labelWidth : 80,
			region:"center",
			layout:"column",
			bodyStyle:'padding:5px 0 0',
			frame:true,
			items:[{
				columnWidth:.5,
				layout:"form",
				defaults:{
					anchor:"95%",
					xtype:"textfield",
					labelSeparator:""
				},
				items:[
					{
						xtype:"hidden",
						id:"id",
						name:"logistics.id"
					},{
						id:"name",
						name:"logistics.name",
						itemCls:"required",
						allowBlank:false,
						fieldLabel:"物流名称"
					},{
						id:"phone",
						name:"logistics.phone",
						fieldLabel:"联系电话"
					}
				]
			},{
				columnWidth:.5,
				layout:"form",
				defaults:{
					anchor:"95%",
					xtype:"textfield",
					labelSeparator:""
				},
				items:[
					{
						id:"address",
						name:"logistics.address",
						fieldLabel:"物流地址"
					},{
						id:"routes",
						name:"logistics.routes",
						fieldLabel:"物流路线"
					}
				]
			},{
				columnWidth:1,
				layout:"form",
				defaults:{
					anchor:"98%",
					xtype:"textfield",
					labelSeparator:""
				},
				items:[
					{
						id:"arrivalArea",
						name:"logistics.arrivalArea",
						fieldLabel:"到货区域"
					},{
						xtype:"textarea",
						id:"remark",
						name:"logistics.remark",
						fieldLabel:"备注"
					}
				]
			}],
			buttons:[{
				text:"保存",
				handler:this.saveForm,
				scope:this
			},{
				text:"关闭",
				handler:function(){
					Ext.getCmp(Const.LOGISTICS_WIN).close();
				}
			}]
		};
		
		caiban.easyadmin.orders.logistics.LogisticsForm.superclass.constructor.call(this,c);
	},
	saveUrl:Context.ROOT+"/orders/logistics!createLogistics.act",
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
		Ext.getCmp(Const.LOGISTICS_GRID).getStore().reload();
		Ext.getCmp(Const.LOGISTICS_WIN).close();
	},
	onSaveFailure:function(_form,_action){
		Ext.MessageBox.show({
			title:MESSAGE.title,
			msg : MESSAGE.saveFailure,
			buttons:Ext.MessageBox.OK,
			icon:Ext.MessageBox.ERROR
		});
	},
	loadLogistics:function(_id){
		var _fields = [{
			name : "logistics.id",
			mapping : "id"
		}, {
			name : "logistics.name",
			mapping : "name"
		}, {
			name : "logistics.phone",
			mapping : "phone"
		}, {
			name : "logistics.address",
			mapping : "address"
		}, {
			name : "logistics.routes",
			mapping : "routes"
		}, {
			name : "logistics.arrivalArea",
			mapping : "arrivalArea"
		}, {
			name : "logistics.remark",
			mapping : "remark"
		}];
		var form = this;
		var _store = new Ext.data.JsonStore({
			root : "records",
			fields : _fields,
			url : Context.ROOT + "/orders/logistics!listOneLogistics.act?logistics.id="+_id,
			autoLoad : true,
			listeners : {
				"datachanged" : function() {
					var record = _store.getAt(0);
					if (record == null) {
						Ext.MessageBox.alert(MESSAGE.title,MESSAGE.loadError);
						Ext.getCmp(Const.LOGISTICS_WIN).close();
					} else {
						form.getForm().loadRecord(record);
					}
				}
			}
		});
		
	}
});

caiban.easyadmin.orders.logistics.CreateLogisticsWin=function(){
	var form = new caiban.easyadmin.orders.logistics.LogisticsForm({
		saveUrl:Context.ROOT+"/orders/logistics!createLogistics.act",
		region:"center"
	});
	
	var win = new Ext.Window({
		id:Const.LOGISTICS_WIN,
		title:"添加物流信息",
		width:550,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	win.show();
}

caiban.easyadmin.orders.logistics.UpdateLogisticsWin=function(_id){
	var form = new caiban.easyadmin.orders.logistics.LogisticsForm({
		saveUrl:Context.ROOT+"/orders/logistics!updateLogistics.act",
		region:"center"
	});
	
	form.loadLogistics(_id);
	
	var win = new Ext.Window({
		id:Const.LOGISTICS_WIN,
		title:"编辑物流信息",
		width:550,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	
	win.show();
}