Ext.namespace("caiban.easyadmin.eshop");

var Const = new function(){
	this.BUYER_GRID="buyergrid";
	this.BUYER_FORM="buyerform";
	this.BUYER_WIN="buyerwin";
}

Ext.onReady(function(){
	var grid=new caiban.easyadmin.eshop.BuyerGrid({
		id:Const.BUYER_GRID,
		region:"center",
		layout:"fit"
	});
	
	var viewport = new Ext.Viewport({
		layout:"border",
		items:[grid]
	});
});

caiban.easyadmin.eshop.BuyerGrid = Ext.extend(Ext.grid.GridPanel,{
	queryUrl:Context.ROOT+"/eshop/buyer!listBuyer.act",
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
			header:"客户姓名",
			dataIndex:"name"
		},{
			header:"联系电话",
			dataIndex:"phone"
		},{
			header:"email",
			dataIndex:"email"
		},{
			header:"即时通讯",
			dataIndex:"im"
		},{
			header:"评价",
			dataIndex:"valuable",
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				if(value!=null && value!=""){
					return value;
				}
			}
		},{
			header:"备注",
			dataIndex:"remark",
			hidden:true
		},{
			header:"添加人",
			dataIndex:"uid",
			hidden:true
		},{
			header:"添加时间",
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
				handler:function(btn){
					caiban.easyadmin.eshop.CreateBuyerWin();
				}
			},{
				iconCls:"edit16",
				text:"编辑",
				handler:function(btn){
					// TODO 编辑物流信息
					var selectedRecord = Ext.getCmp(Const.BUYER_GRID).getSelectionModel().getSelected();
					if(typeof(selectedRecord)=="undefined"){
						Ext.MessageBox.show({
							title:MESSAGE.title,
							msg : MESSAGE.needOneRecord,
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.WARNING
						});
						return false;
					}
					caiban.easyadmin.eshop.UpdateBuyerWin(selectedRecord.get("id"));
				}
			},{
				iconCls:"delete16",
				text:"删除",
				handler:function(btn){
					var grid = Ext.getCmp(Const.BUYER_GRID);
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
								url:Context.ROOT+"/eshop/buyer!deleteBuyer.act",
								params:{"idArray":_ids.join(",")},
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
				xtype:"checkbox",
				boxLabel:"只显示我添加的",
				ref:"../onlyShowByMe",
				scope:this,
				handler:function(btn){
//					alert(this.onlyShowByMe.getValue())
					var B={};
					if(this.onlyShowByMe.getValue()){
						B={"eshopBuyer.uid":Ext.get("authuserid").dom.value};
					}
					var _store = this.getStore();
					_store.baseParams = B;
					//reload的时候与autoLoad一样,可以重新设置分页变量,对pagingToolBar的更新也需要此配置
					_store.reload({params:{"page.start":0,"page.limit":Context.PAGE_SIZE}});  
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
		
		caiban.easyadmin.eshop.BuyerGrid.superclass.constructor.call(this,c);
	},
	fieldsRecord:["id","name","phone","email","im","valuable","remark","gmtcreate"]
});

//编辑用的表单
caiban.easyadmin.eshop.BuyerForm = Ext.extend(Ext.form.FormPanel,{
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
						name:"eshopBuyer.id"
					},{
						id:"name",
						name:"eshopBuyer.name",
						itemCls:"required",
						allowBlank:false,
						fieldLabel:"买家姓名"
					},{
						id:"email",
						name:"eshopBuyer.email",
						fieldLabel:"Email"
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
						id:"phone",
						name:"eshopBuyer.phone",
						fieldLabel:"联系电话"
					},{
						id:"im",
						name:"eshopBuyer.im",
						fieldLabel:"即时通讯"
					},{
						xtype:"combo",
						id:"valuable",
						name:"valuable",
						hiddenName:"eshopBuyer.valuable",
						fieldLabel:"评价",
						triggerAction:"all",
						typeAhead: true,
						mode		: "remote",
						store:new Ext.data.JsonStore({
							autoLoad	: true,
							root		: "records",
							url			: Context.ROOT+"/eshop/buyer!valuableCombo.act",
							fields		: ["key","value"]
						}),
						valueField:"key",
						displayField:"value"
//						load :function(){
//							
//						}
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
						xtype:"textarea",
						id:"remark",
						name:"eshopBuyer.remark",
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
					Ext.getCmp(Const.BUYER_WIN).close();
				}
			}]
		};
		
		caiban.easyadmin.eshop.BuyerForm.superclass.constructor.call(this,c);
	},
	saveUrl:Context.ROOT+"/eshop/buyer!createBuyer.act",
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
		Ext.getCmp(Const.BUYER_GRID).getStore().reload();
		Ext.getCmp(Const.BUYER_WIN).close();
	},
	onSaveFailure:function(_form,_action){
		Ext.MessageBox.show({
			title:MESSAGE.title,
			msg : MESSAGE.saveFailure,
			buttons:Ext.MessageBox.OK,
			icon:Ext.MessageBox.ERROR
		});
	},
	loadOneRecord:function(_id){
		var _fields = [{
			name : "eshopBuyer.id",
			mapping : "id"
		}, {
			name : "eshopBuyer.name",
			mapping : "name"
		}, {
			name : "eshopBuyer.phone",
			mapping : "phone"
		}, {
			name : "eshopBuyer.email",
			mapping : "email"
		}, {
			name : "eshopBuyer.im",
			mapping : "im"
		}, {
			name : "eshopBuyer.remark",
			mapping : "remark"
		}, {
			name : "eshopBuyer.valuable",
			mapping : "valuable"
		}];
		var form = this;
		var _store = new Ext.data.JsonStore({
			root : "records",
			fields : _fields,
			url : Context.ROOT + "/eshop/buyer!listOneBuyer.act?eshopBuyer.id="+_id,
			autoLoad : true,
			listeners : {
				"datachanged" : function() {
					var record = _store.getAt(0);
					if (record == null) {
						Ext.MessageBox.alert(MESSAGE.title,MESSAGE.loadError);
						Ext.getCmp(Const.BUYER_WIN).close();
					} else {
						form.getForm().loadRecord(record);
					}
				}
			}
		});
		
	}
});

caiban.easyadmin.eshop.CreateBuyerWin=function(){
	var form = new caiban.easyadmin.eshop.BuyerForm({
		saveUrl:Context.ROOT+"/eshop/buyer!createBuyer.act",
		region:"center"
	});
	
	var win = new Ext.Window({
		id:Const.BUYER_WIN,
		title:"添加客户信息",
		width:550,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	win.show();
}

caiban.easyadmin.eshop.UpdateBuyerWin=function(_id){
	var form = new caiban.easyadmin.eshop.BuyerForm({
		saveUrl:Context.ROOT+"/eshop/buyer!updateBuyer.act",
		region:"center"
	});
	
	form.loadOneRecord(_id);
	
	var win = new Ext.Window({
		id:Const.BUYER_WIN,
		title:"编辑客户信息",
		width:550,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	
	win.show();
}