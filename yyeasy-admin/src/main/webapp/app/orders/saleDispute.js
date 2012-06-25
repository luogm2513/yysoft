Ext.namespace("caiban.easyadmin.orders.saledispute");

/**
 * 用来定义全局常量，一般用来定义组件的ID号
 * */
var SALEDISPUTE=new function(){
	this.EDITSALEDISPUTE_FORM = "editsaledisputeform";
	this.EDITSALEDISPUTE_WIN = "editsaledisputewin";
	this.SALEDISPUTE_GRID = "saledisputegrid";
}

caiban.easyadmin.orders.saledispute.SaleDisputeGrid = Ext.extend(Ext.grid.GridPanel, {
	queryUrl:Context.ROOT+"/orders/saleDispute!list.act",
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
			autoLoad:false
		});
		
		var grid = this;
		
		var _sm=new Ext.grid.CheckboxSelectionModel({
			listeners: {
	            // On selection change, set enabled state of the removeButton
	            // which was placed into the GridPanel using the ref config
	            selectionchange: function(sm) {
	            	if (sm.getCount()) {
	            		grid.editButton.enable();
	            		grid.deleteButton.enable();
	            	} else {
	                    grid.editButton.disable();
	                    grid.deleteButton.disable();
	                }
	            }
	        }	
		});

		var _cm=new Ext.grid.ColumnModel([_sm,{
			header:"编号",
			width:10,
			sortable:true,
			dataIndex:"id",
			hidden:true
		},{
			header:"订单编号",
			dataIndex:"orderNo"
		},{//这里同时显示已支付金额，格式：总金额(已支付)
			header:"处理情况",
			dataIndex:"resolvedStr"
		},{
			header:"退货?",
			dataIndex:"returnGoods"
		},{
			header:"赔偿金额",
			dataIndex:"reparation"
		},{
			header:"严重程度",
			dataIndex:"severityStr"
		},{
			header:"处理人",
			dataIndex:"username"
		},{
			header:"处理期限",
			dataIndex:"period",
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				if(value!=null && value!=""){
					return value;
				}
			}
		},{
			header:"创建时间",
			sortable:true,
			dataIndex:"saleDispute.gmtcreate",
			hidden:true,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				if(value!=null && value!=""){
					return value;
				}
			}
		}]);
		
		var c={
//			iconCls:"icon-grid",
			viewConfig:{
				autoFill:true
			},
			store:_store,
			sm:_sm,
			cm:_cm,
			tbar:[{
				iconCls:"edit16",
				tooltip:"更新纠纷处理情况",
				disabled: true,
				ref:"../editButton",
				handler:function(btn){
					//TODO 更新纠纷处理信息
					var selectedRecord = grid.getSelectionModel().getSelected();
					if(typeof(selectedRecord)=="undefined"){
						Ext.MessageBox.show({
							title:MESSAGE.title,
							msg : MESSAGE.needOneRecord,
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.WARNING
						});
						return false;
					}
					caiban.easyadmin.orders.saledispute.EditSaleDisputeWin(selectedRecord.get("id"),selectedRecord.get("orderNo"));
				}
			},{
				iconCls:"delete16",
				tooltip:"删除纠纷",
				disabled: true,
				ref:"../deleteButton",
				handler:function(btn){
					var selectedRecord = grid.getSelectionModel().getSelected();
					if(typeof(selectedRecord)=="undefined"){
						Ext.MessageBox.show({
							title:MESSAGE.title,
							msg : MESSAGE.needOneRecord,
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.WARNING
						});
						return false;
					}
					Ext.MessageBox.confirm(MESSAGE.title, MESSAGE.confirmDelete, function(btn){
						if(btn != "yes"){
							return false;
						}
						caiban.easyadmin.orders.saledispute.DeleteSaleDispute(grid);
					});
				}
			},"->",{
				xtype:"checkbox",
				boxLabel:"新建",
				ref:"../newDisputeButton",
				checked:true,
				handler:function(btn){
					grid.listByResolvedParam();
				}
			},{
				xtype:"checkbox",
				boxLabel:"进行中",
				ref:"../inprocessDisputeButton",
				checked:true,
				handler:function(btn){
					grid.listByResolvedParam();
				}
			},'-',{
				xtype:"combo",
				id:"resolvedSearch",
				ref:"../resolvedDisputeButton",
				width:120,
				triggerAction:"all",
				typeAhead: true,
				mode		: "remote",
				store:new Ext.data.JsonStore({
					autoLoad	: false,
					root		: "records",
					url			: Context.ROOT+"/orders/saleDispute!resolvedCombo.act",
					fields		: ["key","value"]
				}),
				valueField:"key",
				displayField:"value",
				listeners:{
//		         	'select': function(obj){
//						grid.listByResolvedParam();
//					},
					'change':function(obj){
						grid.listByResolvedParam();
					}
				}
			},'-',{
				xtype:"checkbox",
				boxLabel:"只显示我的订单",
				ref:"../onlyShowByMe",
//				scope:this,
				handler:function(btn){
//					var _store = Ext.getCmp(SALEDISPUTE.ORDERS_GRID).getStore();
					var B=_store.baseParams||{};
//					
					if(btn.getValue()){
						B["onlyme"]="Y";
					}else{
						B["onlyme"]="N";
					}
					_store.baseParams = B;
//					//reload的时候与autoLoad一样,可以重新设置分页变量,对pagingToolBar的更新也需要此配置
					_store.reload({params:{"page.start":0,"page.limit":Context.PAGE_SIZE}});
				}
			}],
			paramNames : MESSAGE.paging.paramNames,
			bbar: new Ext.PagingToolbar({
				pageSize : Context.PAGE_SIZE,
				store : _store,
				displayInfo: true,
				displayMsg: MESSAGE.paging.displayMsg,
				emptyMsg : MESSAGE.paging.emptyMsg,
				beforePageText : MESSAGE.paging.beforePageText,
				afterPageText : MESSAGE.paging.afterPageText,
				paramNames : MESSAGE.paging.paramNames
			}),
			listeners:{
				"rowcontextmenu":function(g,rowIndex,e){
					if(!g.getSelectionModel().isSelected(rowIndex)){
						g.getSelectionModel().clearSelections();
						g.getSelectionModel().selectRow(rowIndex);
					}
					e.preventDefault();
					if(g.contextmenu!=null){
						g.contextmenu.showAt(e.getXY());
					}
				}
			}
		};
		
		caiban.easyadmin.orders.saledispute.SaleDisputeGrid.superclass.constructor.call(this,c);
	},
	fieldsRecord:Ext.data.Record.create([{
		name: 'id',
		mapping:'saleDispute.id'
	},{
		name: 'orderNo',
		mapping:'orders.orderNo'
	},{
		name: 'returnGoods',
		mapping:'saleDispute.returnGoods'
	},{
		name: 'reparation',
		mapping:'saleDispute.reparation'
	},{
		name: 'period',
		mapping:'saleDispute.period'
	},{
		name: 'severity',
		mapping:'saleDispute.severity'
	},{
		name:'severityStr',
		mapping:'severityStr'
	},{
		name: 'resolvedStr',
		mapping:'resolvedStr'
	},{
		name: 'resolved',
		mapping:'saleDispute.resolved'
	},{
		name: 'resolvedUid',
		mapping:'saleDispute.resolvedUid'
	},{
		name: 'username',
		mapping:'username'
	},{
		name: 'gmtcreate',
		mapping:'saleDispute.gmtcreate'
	}]),
	listByResolvedParam:function(){
		var B=this.getStore().baseParams||{};
		
		var ary = new Array();
		
		if(this.newDisputeButton.getValue()){
			ary.push(0);
		}
		
		if(this.inprocessDisputeButton.getValue()){
			ary.push(10);
		}
		
		if(this.resolvedDisputeButton.getValue()){
			ary.push(this.resolvedDisputeButton.getValue());
		}
		
		B["resolvedArray"] = ary.join(",");
		
		this.getStore().baseParams = B;
//		//reload的时候与autoLoad一样,可以重新设置分页变量,对pagingToolBar的更新也需要此配置
		this.getStore().reload({params:{"page.start":0,"page.limit":Context.PAGE_SIZE}});
	},
	contextmenu:null
});

/**
 * 售后纠纷申请表单
 * */
caiban.easyadmin.orders.saledispute.SaleDisputeForm = Ext.extend(Ext.form.FormPanel,{
	saveUrl:Context.ROOT+"/orders/orders!createSaleDispute.act",
	constructor:function(config){
		config=config||{};
		Ext.apply(this,config);
//		var form=this;
		
		var c={
			labelAlign : "right",
			labelWidth : 80,
			region:"center",
			bodyStyle:'padding:5px 0 0',
			layout:"column",
			frame:true,
			items:[{
				columnWidth:.5,
				layout:"form",
				defaults:{
					anchor:"95%",
					xtype:"textfield",
					labelSeparator:""
				},
				items:[{
					xtype:"hidden",
					id:"id",
					name:"saleDispute.id"
				},{
					xtype:"hidden",
					id:"orderId",
					name:"saleDispute.orderId"
				},{
					id:"orderNo",
					fieldLabel:"订单号",
					readOnly:true
				},{
					xtype:"checkbox",
					id:"returnGoods",
					name:"saleDispute.returnGoods",
					fieldLabel:"需要退货？",
					inputValue:1
				},{
					xtype:"combo",
					id:"resolved",
//						name:"saleDispute.severity",
					fieldLabel:"处理状态",
					hiddenName:"saleDispute.resolved",
					triggerAction:"all",
					typeAhead: true,
					mode		: "remote",
					store:new Ext.data.JsonStore({
						autoLoad	: true,
						root		: "records",
						url			: Context.ROOT+"/orders/saleDispute!resolvedCombo.act",
						fields		: ["key","value"]
					}),
					valueField:"key",
					displayField:"value"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				defaults:{
					anchor:"95%",
					xtype:"textfield",
					labelSeparator:""
				},
				items:[{
					xtype:"combo",
					id:"severity",
					fieldLabel:"严重程度",
					hiddenName:"saleDispute.severity",
					triggerAction:"all",
					typeAhead: true,
					mode		: "remote",
					store:new Ext.data.JsonStore({
						autoLoad	: true,
						root		: "records",
						url			: Context.ROOT+"/orders/saleDispute!severityCombo.act",
						fields		: ["key","value"]
					}),
					valueField:"key",
					displayField:"value"
				},{
					xtype:"numberfield",
					id:"reparation",
					name:"saleDispute.reparation",
					fieldLabel:"赔偿(元)",
					value:0
				},{
					xtype:"datefield",
					id:"period",
					name:"saleDispute.period",
					format: "Y-m-d",
					fieldLabel:"处理期限",
					value:new Date()
				}]
			},{
				columnWidth:1,
				layout:"fit",
				defaults:{
					anchor:"95%",
					labelSeparator:""
				},
				items:[{
					xtype:"htmleditor",
					id:"remark",
					name:"saleDispute.remark"
				}]
			}],
			buttons:[{
				text:"保存",
				handler:this.saveForm,
				scope:this
			},{
				text:"取消",
				scope:this,
				handler:this.cancelSubmit
//						function(btn){
////						Ext.getCmp(ORDERS.LOGISTICS_DETAILS_WIN).close();
//					}
			}]
		};
		
		caiban.easyadmin.orders.saledispute.SaleDisputeForm.superclass.constructor.call(this,c);
	},
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
	cancelSubmit:function(){
		
	},
	onSaveSuccess:function(_form,_action){
		//TODO 成功后的处理方法，可以在调用时改写
//		Ext.getCmp(ORDERS.ORDERS_GRID).getStore().reload();
//		Ext.getCmp(ORDERS.LOGISTICS_DETAILS_WIN).close();
	},
	onSaveFailure:function(_form,_action){
		Ext.MessageBox.show({
			title:MESSAGE.title,
			msg : MESSAGE.saveFailure,
			buttons:Ext.MessageBox.OK,
			icon:Ext.MessageBox.ERROR
		});
	},
	initForm:function(ordersId,ordersNo){
		this.findById("orderId").setValue(ordersId);
		this.findById("orderNo").setValue(ordersNo);
	},
	loadOneRecord:function(_id,_orderNo){
		var _fields = [{
				name:"saleDispute.id",
				mapping:"id"
			},{
				name:"saleDispute.orderId",
				mapping:"orderId"
			},{
				name:"saleDispute.returnGoods",
				mapping:"returnGoods"
			},{
				name:"saleDispute.resolved",
				mapping:"resolved"
			},{
				name:"saleDispute.severity",
				mapping:"severity"
			},{
				name:"saleDispute.reparation",
				mapping:"reparation"
			},{
				name:"saleDispute.period",
				mapping:"period"
			},{
				name:"saleDispute.remark",
				mapping:"remark"
			},{
				name:"saleDispute.orderNo",
				mapping:"orderNo"
			}];
		var form = this;
		var _store = new Ext.data.JsonStore({
			root : "records",
			fields : _fields,
			url : Context.ROOT + "/orders/saleDispute!listOneSaleDispute.act?saleDispute.id="+_id,
			autoLoad : true,
			listeners : {
				"datachanged" : function() {
					var record = _store.getAt(0);
					if (record == null) {
						Ext.MessageBox.alert(MESSAGE.title,MESSAGE.loadError);
//						Ext.getCmp(ORDERS.ORDERS_WIN).close();
					} else {
						form.getForm().loadRecord(record);
						//初始化订单号
						form.findById("orderNo").setValue(_orderNo);
					}
				}
			}
		});
		
	}
});

caiban.easyadmin.orders.saledispute.EditSaleDisputeWin = function(id,orderNo){
	var form = new caiban.easyadmin.orders.saledispute.SaleDisputeForm({
		id:SALEDISPUTE.EDITSALEDISPUTE_FORM,
		saveUrl:Context.ROOT+"/orders/saleDispute!updateSaleDispute.act",
		region:"center",
		onSaveSuccess:function(_form,_action){
			Ext.getCmp(SALEDISPUTE.SALEDISPUTE_GRID).getStore().reload();
			Ext.getCmp(SALEDISPUTE.EDITSALEDISPUTE_WIN).close();
		},
		cancelSubmit:function(){
			Ext.getCmp(SALEDISPUTE.EDITSALEDISPUTE_WIN).close();
		}
	});
	
	form.loadOneRecord(id,orderNo);
	
	var win = new Ext.Window({
		id:SALEDISPUTE.EDITSALEDISPUTE_WIN,
		title:"处理纠纷",
		width:600,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	
	win.show();
}

/**
 * 删除没用的纠纷信息
 * */
caiban.easyadmin.orders.saledispute.DeleteSaleDispute = function (grid){
	//TODO 删除纠纷信息
	var row = grid.getSelectionModel().getSelections();
	var _ids = new Array();
	for (var i=0,len = row.length;i<len;i++){
		var _id=row[i].get("id");
		_ids.push(_id);
	}
	
	//删除纠纷信息
	Ext.Ajax.request({
		url:Context.ROOT+"/orders/saleDispute!deleteSaleDispute.act",
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
}

/**
 * 快速更改纠纷处理的状态
 * @param grid:纠纷表格
 * @param targetStatus:目标状态
 */
caiban.easyadmin.orders.saledispute.AssignResolved = function(grid, targetStatus){
	var sm=grid.getSelectionModel();
	var submitIds=sm.getCount();
	if ( submitIds > 0){
		var row = sm.getSelections();
		var _ids = new Array();
		for (var i=0,len = row.length;i<len;i++){
			var _id=row[i].get("id");
			_ids.push(_id);
		}
		
		Ext.Ajax.request({
			url:Context.ROOT+"/orders/saleDispute!updateSaleDisputeResolved.act",
			params:{"idArray":_ids.join(","),"saleDispute.resolved":targetStatus},
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
	}
}

/**
 * 快速分配纠纷问题给处理人员
 * ＠param grid:纠纷表格
 * @param targetUser:分配的目标
 * */
caiban.easyadmin.orders.saledispute.AssignResolvedUser = function(grid, targetUser){
	//TODO 分配纠纷给工作人员处理
}
