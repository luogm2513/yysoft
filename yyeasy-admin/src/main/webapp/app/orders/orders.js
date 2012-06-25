Ext.namespace("caiban.easyadmin.orders");

var ORDERS = new function(){
	this.ORDERS_WIN="orderswin";
//	this.LOGISTICS_GRID = "logisticsgrid";
	this.ORDERS_GRID = "ordersgrid";
	this.NEWORDERS_FORM="newordersform";
	this.PRODUCT_LIST="productlist";
	this.LOGISTICS_DETAILS_WIN="logisticsdetailswin";
	this.NEWSALEDISPUTE_FORM = "newsaledisputeform";
	this.NEWSALEDISPUTE_WIN = "newsaledisputewin";
}

caiban.easyadmin.orders.OrdersGrid = Ext.extend(Ext.grid.GridPanel, {
	queryUrl:Context.ROOT+"/orders/orders!listOrders.act",
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
	                    grid.removeButton.enable();
	                    grid.editButton.enable();
	                    grid.deliveryOrders.enable();
	                    grid.arrivalConfirm.enable();
	                    grid.disputeButton.enable();
	                } else {
	                    grid.removeButton.disable();
	                    grid.editButton.disable();
	                    grid.deliveryOrders.disable();
	                    grid.arrivalConfirm.disable();
	                    grid.disputeButton.disable();
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
		},{
			header:"淘宝订单号",
			dataIndex:"orderNoTaobao"
		},{
			header:"订单总额",
			dataIndex:"totalOrderCash"
		},{
			header:"产品总额",
			dataIndex:"totalProductCash",
			hidden:true
		},{//这里同时显示已支付金额，格式：总金额(已支付)
			header:"已支付",
			dataIndex:"paid"
		},{
			header:"客户",
			dataIndex:"buyerName"
		},{
			header:"支付方式",
			dataIndex:"paymentStr"
		},{//可以用图片（加title）来表示
			header:"订单状态",
			dataIndex:"orderStatusStr",
			sortable:true
		},{
			header:"添加者",
			dataIndex:"uid",
			hidden:true
		},{
			header:"下单时间",
			sortable:true,
			dataIndex:"gmtOrder",
			hidden:true,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				if(value!=null && value!=""){
					return value;
				}
			}
		},{
			header:"创建时间",
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
//			iconCls:"icon-grid",
			viewConfig:{
				autoFill:true
			},
			store:_store,
			sm:_sm,
			cm:_cm,
			tbar:[{
				iconCls:"add16",
				tooltip:"新订单",
				handler:function(btn){
					caiban.easyadmin.orders.CreateOrdersWin();
				}
			},{
				iconCls:"edit16",
				tooltip:"更新订单,你可以在这里更改付款信息",
				disabled: true,
				ref:"../editButton",
				handler:function(btn){
					var selectedRecord = Ext.getCmp(ORDERS.ORDERS_GRID).getSelectionModel().getSelected();
					if(typeof(selectedRecord)=="undefined"){
						Ext.MessageBox.show({
							title:MESSAGE.title,
							msg : MESSAGE.needOneRecord,
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.WARNING
						});
						return false;
					}
					caiban.easyadmin.orders.UpdateOrdersWin(selectedRecord.get("id"));
				}
			},{
				iconCls:"delete16",
				tooltip:"删除订单,只允许删除特定条件下的订单",
				disabled: true,
				ref:"../removeButton",
				handler:function(btn){
					// TODO 只允许删除特定条件下的订单
					var grid = Ext.getCmp(ORDERS.ORDERS_GRID);
					var sm=grid.getSelectionModel();
					var submitIds=sm.getCount();
					if ( submitIds== 0){
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
								url:Context.ROOT+"/orders/orders!deleteOrders.act",
								params:{"ordersArray":_ids.join(",")},
								success:function(response,opt){
									var obj = Ext.decode(response.responseText);
									if(obj.success){
										grid.getStore().reload();
										if(submitIds!=obj.data){
											Ext.MessageBox.show({
												title:MESSAGE.title,
												msg : "提交删除的"+submitIds+"份订单，实际只删除了"+obj.data+"份<br/>注：只有未发货的订单可以被删除",
												buttons:Ext.MessageBox.OK,
												icon:Ext.MessageBox.WARNING
											});
										}
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
			},"-",{
				text:"发货",
				ref:"../deliveryOrders",
				disabled: true,
				handler:function(btn){
					var grid = Ext.getCmp(ORDERS.ORDERS_GRID);
					var sm=grid.getSelectionModel();
					var submitIds=sm.getCount();
					if ( submitIds== 0){
						Ext.MessageBox.show({
							title:MESSAGE.title,
							msg : MESSAGE.needOneRecord,
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.WARNING
						});
					} else{
						caiban.easyadmin.orders.DeliveryOrdersWin(sm.getSelected().get("id"),sm.getSelected().get("orderNo"));
					}
				}
			}
//			,{//订单一旦确定，将不允许修改
////				iconCls:"item-edit",
//				text:"付款",
//				handler:function(btn){
//					// TODO 暂时只允许一次对一个订单付款，付款时可以先支付一部分，
//					// 也可以全额付款(全额可能是指剩下的所有款项)，实际设置paid项
//					// 付款时也可以同时更改支付方式
//				}
//			}
//			,{
////				iconCls:"item-del",
//				text:"物流情况",
//				handler:function(btn){
//					// TODO 允许查看交易状态中的订单物流情况
//				}
//			}
			,{
//				iconCls:"item-del",
				text:"交易失败",  //添加到纠纷处理流程
				disabled: true,
				ref:"../disputeButton",
				handler:function(btn){
					// TODO 交易失败时将对订单做相应处理，进入客服纠纷处理流程
//					var grid = Ext.getCmp(ORDERS.ORDERS_GRID);
					var sm=grid.getSelectionModel();
					caiban.easyadmin.orders.NewSaleDisputeWin(sm.getSelected().get("id"),sm.getSelected().get("orderNo"));
				}
			},{
//				iconCls:"item-del",
				text:"交易成功",
				disabled: true,
				ref:"../arrivalConfirm",
				handler:function(btn){
					// TODO 交易成功时改变订单状态即可
//					var grid = Ext.getCmp(ORDERS.ORDERS_GRID);
					var sm=grid.getSelectionModel();
//					var submitIds=sm.getCount();
//					if ( submitIds== 0){
//						Ext.MessageBox.show({
//							title:MESSAGE.title,
//							msg : MESSAGE.needOneRecord,
//							buttons:Ext.MessageBox.OK,
//							icon:Ext.MessageBox.WARNING
//						});
//					} else{
					Ext.MessageBox.confirm(MESSAGE.title, "确定要将这个订单标记为交易成功,并结束该订单吗?", function(btn){
						if(btn != "yes"){
							return false;
						}
						caiban.easyadmin.orders.DealSuccess(sm.getSelected().get("id"), grid);
					});
//					}
				}
			},"->",{
				text:"订单状态",
				menu:new Ext.menu.Menu({
					id:"statusMenu",
					items:orderStatusMenu
				})
			},"-",{
				xtype:"checkbox",
				boxLabel:"只显示我的订单",
				ref:"../onlyShowByMe",
				scope:this,
				handler:function(btn){
					var _store = Ext.getCmp(ORDERS.ORDERS_GRID).getStore();
					var B=_store.baseParams||{};
					
					if(btn.getValue()){
						B["onlyme"]="Y";
					}else{
						B["onlyme"]="N";
					}
					
	//				var tree=Ext.getCmp(ORDERS.CATEGORY_TREE);
	//				var node = tree.getSelectionModel().getSelectedNode();
	//				
	//				if(node!=null && node.attributes.data>0){
	//					B["productCategory.id"]=node.attributes.data;
	//				}
					
					_store.baseParams = B;
					//reload的时候与autoLoad一样,可以重新设置分页变量,对pagingToolBar的更新也需要此配置
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
			})
		};
		
		caiban.easyadmin.orders.OrdersGrid.superclass.constructor.call(this,c);
	},
	fieldsRecord:Ext.data.Record.create([{
		name: 'id',
		mapping:'orders.id'
	},{
		name: 'orderNo',
		mapping:'orders.orderNo'
	},{
		name: 'orderNoTaobao',
		mapping:'orders.orderNoTaobao'
	},{
		name: 'buyerName',
		mapping:'buyer.name'
	},{
		name: 'buyerId',
		mapping:'buyer.id'
	},{
		name: 'payment',
		mapping:'orders.payment'
	},{
		name:'paymentStr',
		mapping:'paymentStr'
	},{
		name: 'totalOrderCash',
		mapping:'orders.totalOrderCash'
	},{
		name: 'totalProductCash',
		mapping:'orders.totalProductCash'
	},{
		name: 'paid',
		mapping:'orders.paid'
	},{
		name: 'gmtOrder',
		mapping:'orders.gmtOrder'
	},{
		name: 'orderStatus',
		mapping:'orders.orderStatus'
	},{
		name: 'orderStatusStr',
		mapping:'orderStatusStr'
	},{
		name: 'uid',
		mapping:'orders.uid'
	},{
		name: 'gmtcreate',
		mapping:'orders.gmtcreate'
	}])
});

caiban.easyadmin.orders.NewOrdersForm =Ext.extend(Ext.form.FormPanel,{
	isEdit:false,
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		var form = this;
		var _isEdit=this.isEdit;
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
						name:"orders.id"
					},{
						id:"orderNo",
						name:"orders.orderNo",
						itemCls:"required",
						allowBlank:false,
						fieldLabel:"订单号",
						readOnly:_isEdit,
						value:Date.parse(new Date())+parseInt(Math.random()*10000)
					},{
						xtype:"combo",
						id:"payment",
						name:"payment",
						hiddenName:"orders.payment",
						fieldLabel:"支付方式",
						triggerAction:"all",
						typeAhead: true,
						mode		: "remote",
						store:new Ext.data.JsonStore({
							autoLoad	: true,
							root		: "records",
							url			: Context.ROOT+"/orders/orders!paymentCombo.act",
							fields		: ["key","value"]
						}),
						valueField:"key",
						displayField:"value"
					},new caiban.easyadmin.orders.EshopBuyerCombo({
						id:"buyerName",
						name:"eshopBuyer.name",
						fieldLabel:"客户",
						itemCls:"required",
						allowBlank:false,
						readOnly:_isEdit,
						onSelect:function(records){
							this.setValue(records.get("name"));
							form.findById("buyerId").setValue(records.get("id"));
							form.findById("phone").setValue(records.get("phone"));
				        	this.collapse();
						}
					}),{
						xtype:"hidden",
						id:"buyerId",
						name:"orders.buyerId"
					},{
						xtype:"hidden",
						id:"totalProductCash",
						name:"orders.totalProductCash"
					},{
						xtype:"hidden",
						id:"orderStatus",
						name:"orders.orderStatus"
					},{
						xtype:"numberfield",
						id:"totalOrderCash",
						name:"orders.totalOrderCash",
						fieldLabel:"总金额(元)"
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
						id:"orderNoTaobao",
						name:"orders.orderNoTaobao",
						fieldLabel:"淘宝订单号"
					},{
						xtype:"datefield",
						id:"gmtOrder",
						name:"orders.gmtOrder",
						format: "Y-m-d",
						fieldLabel:"下单时间",
						readOnly:_isEdit
					},{
						id:"phone",
						name:"eshopBuyer.phone",
						readOnly:_isEdit,
						fieldLabel:"客户电话"
					},{
						xtype:"numberfield",
						id:"paid",
						name:"orders.paid",
						fieldLabel:"已支付(元)"
					}
				]
			},{
				xtype:"tabpanel",
				id:"tabpanel1",
				columnWidth:1,
				activeTab:0,
				border:false,
				defaults:{
					height:200
				},
				items:[
					new caiban.easyadmin.orders.ProductList({
						id:ORDERS.PRODUCT_LIST,
						title:'产品',
						listeners:{
							"afteredit":function(e){
								Ext.getCmp("productaddfield").focus();
							}
						}
					}),
					{title:"物流情况"}
				]
			}],
			buttons:[{
				text:"保存",
				handler:this.saveForm,
				scope:this
			},{
				text:"关闭",
				handler:function(){
					Ext.getCmp(ORDERS.ORDERS_WIN).close();
				}
			}]
			
		};
		
		caiban.easyadmin.orders.NewOrdersForm.superclass.constructor.call(this,c);
	},
	saveUrl:Context.ROOT+"/orders/orders!createNewOrders.act",
	saveForm:function(){
		if(this.getForm().isValid()){
			var _s = Ext.getCmp(ORDERS.PRODUCT_LIST).getStore();
			if(_s.getCount()==0){
				Ext.getCmp("product-statusbar").setStatus({
					text:"<span style='color:red;'>请给订单增加产品信息。</span>"
				});
				Ext.getCmp("productaddfield").focus();
			}else{
				var totalproductcash = 0;
				//TODO 产品订单总额
				for(var i=0;i<_s.getCount();i++){
					if(_s.getAt(i).get("priceSale") != "" && _s.getAt(i).get("quantity")!=""){
						totalproductcash = totalproductcash + 
							(_s.getAt(i).get("priceSale")*_s.getAt(i).get("quantity")); 
					}
				}
				Ext.getCmp("totalProductCash").setValue(totalproductcash);
//				if(this.findById("buyerName").getValue()==""){
//					Ext.getCmp("buyerId").setValue("");
//				}
//				alert(this.findById("buyerId").getValue())
				this.getForm().submit({
					url:this.saveUrl,
					method:"post",
					type:"json",
					success:this.onSaveSuccess,
					failure:this.onSaveFailure,
					scope:this
				});
			}
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
		//TODO 保存订单的产品信息
//		alert(_action.result.data);
		var s= Ext.getCmp(ORDERS.PRODUCT_LIST).getStore();
		for(var i=0;i<s.getCount();i++){
			var record = s.getAt(i);
			Ext.Ajax.request({
				url:Context.ROOT+"/orders/orders!addOrdersProduct.act",
				params:{
					"ordersProduct.productId":record.get("productId"),
					"ordersProduct.ordersId":_action.result.data,
					"ordersProduct.quantity":record.get("quantity"),
					"ordersProduct.priceDiscount":record.get("priceDiscount")
				},
				success:function(response,opt){
					var obj = Ext.decode(response.responseText);
					if(!obj.success){
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
		
		Ext.getCmp(ORDERS.ORDERS_GRID).getStore().reload();
		Ext.getCmp(ORDERS.ORDERS_WIN).close();
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
			name: "orders.id"
		}, {
			name : "orders.orderNo"
		}, {
			name : "orders.orderNoTaobao"
		}, {
			name : "orders.buyerId"
		}, {
			name : "orders.payment"
		}, {
			name : "orders.totalOrderCash"
		}, {
			name : "orders.totalProductCash"
		}, {
			name : "orders.paid"
		}, {
			name : "orders.gmtOrder"
		}, {
			name : "orders.orderStatus"
		}, {
			name : "orders.uid"
		}, {
			name : "orders.eid"
		}, {
			name : "orders.gmtcreate"
		}, {
			name : "buyer.id"
		}, {
			name : "eshopBuyer.name",
			mapping:"buyer.name"
		}, {
			name : "eshopBuyer.phone",
			mapping:"buyer.phone"
		}];
		var form = this;
		var _store = new Ext.data.JsonStore({
			root : "records",
			fields : _fields,
			url : Context.ROOT + "/orders/orders!listOneOrders.act?orders.id="+_id,
			autoLoad : true,
			listeners : {
				"datachanged" : function() {
					var record = _store.getAt(0);
					if (record == null) {
						Ext.MessageBox.alert(MESSAGE.title,MESSAGE.loadError);
						Ext.getCmp(ORDERS.ORDERS_WIN).close();
					} else {
						form.getForm().loadRecord(record);
						Ext.getCmp(ORDERS.PRODUCT_LIST).getStore().load({params:{"orders.id":record.data["orders.id"]}});
					}
				}
			}
		});
		
	}
});

caiban.easyadmin.orders.CreateOrdersWin=function(){
	
	var form = new caiban.easyadmin.orders.NewOrdersForm({
		id:ORDERS.NEWORDERS_FORM,
		saveUrl:Context.ROOT+"/orders/orders!createNewOrders.act",
		region:"center"
	});
	
	var win = new Ext.Window({
		id:ORDERS.ORDERS_WIN,
		title:"添加新订单",
		width:650,
		autoHeight:true,
		modal:true,
		items:[form],
		bbar:new Ext.ux.StatusBar({
            id: 'product-statusbar',
            defaultText: '请按提示操作。'
        })
	});
	win.show();
}

caiban.easyadmin.orders.UpdateOrdersWin=function(id){
	var form = new caiban.easyadmin.orders.NewOrdersForm({
		isEdit:true,
		id:ORDERS.NEWORDERS_FORM,
		saveUrl:Context.ROOT+"/orders/orders!updateSimpleOrders.act",
		region:"center"
	});
	form.loadOneRecord(id);
	
	var win = new Ext.Window({
		id:ORDERS.ORDERS_WIN,
		title:"更新订单",
		width:650,
		autoHeight:true,
		modal:true,
		items:[form],
		bbar:new Ext.ux.StatusBar({
            id: 'product-statusbar',
            defaultText: '请按提示操作。'
        })
	});
	win.show();
}

//订单的产品列表
caiban.easyadmin.orders.ProductList = Ext.extend(Ext.grid.EditorGridPanel,{
	dataurl:Context.ROOT+"/orders/orders!loadProductListByOrdersId.act",
	constructor:function(config){
		config=config||{};
		Ext.apply(this,config);
		
		var _url=this.dataurl;
		var _fields = this.porductRecord;
		var _store = new Ext.data.JsonStore({
			root:"records",
			totalProperty:'totals',
			remoteSort:true,
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
			header:'产品名称',
			dataIndex:'productName'
		},{
			header:'销售价格(元)',
			dataIndex:'priceSale'
		},{
			header:'订购数量',
			dataIndex:'quantity',
			editor:{xtype:'numberfield'}
		},{
			header:'折扣(%)',
			dataIndex:'priceDiscount',
			editor:{xtype:'textfield'}
		}]);
		
		var grid = this;
		
		var c={
				iconCls:"money16",
				viewConfig:{
					autoFill:true
				},
				store:_store,
				sm:_sm,
				cm:_cm,
				tbar:["产品名称:",new caiban.easyadmin.orders.ProductCombo({
					id:"productaddfield",
					width:430,
					onSelect:function(records){
						//TODO 将选中的产品信息添加到表格中,并清空输入框内的值
						//如果已存在添加过的产品则在状态栏中提示已经存在
						//否则将产品信息添加到表格中
						var s= grid.getStore();
						for(var i=0;i<s.getCount();i++){
							if(s.getAt(i).get("productId")==records.get("id")){
								//状态栏提示
								Ext.getCmp("product-statusbar").setStatus({
									text:"<span style='color:red;'>您已经选择了产品\"<b>"+records.get("name")+"</b>\"，请重新选一个...</span>"
								});
								this.setValue();
								this.collapse();
								return ;
							}
						}
						
						var ordersid=Ext.getCmp(ORDERS.NEWORDERS_FORM).findById("id").getValue();
						if(typeof(ordersid) =="undefined" || ordersid=="" || ordersid==null){
							ordersid=0;
						}
						var r = new grid.porductRecord({
							id:0,
							productId:records.get("id"),
							productName:records.get("name"),
							ordersId:ordersid,
							priceSale:records.get("priceSale"),
							quantity:1,
							priceDiscount:"100"
						});
						
						grid.stopEditing();
						s.insert(s.getCount(),r);
						grid.startEditing(s.getCount()-1,4);
						
						this.setValue("");
						this.collapse();
					}
				}),"->",{
					iconCls:"delete16",
					handler:function(btn){
						var row = grid.getSelectionModel().getSelections();
						if(row.length <=0 ){
							return false;
						}
						Ext.MessageBox.confirm(MESSAGE.title,MESSAGE.confirmDelete,function(btn){
							if(btn!="yes"){
								return ;
							}
							
							
							for(var i=0;i<row.length;i++){
								grid.getStore().remove(row[i]);
							}
							
						});
					}
				}]
		};
		
		caiban.easyadmin.orders.ProductList.superclass.constructor.call(this,c);
	},
	porductRecord:Ext.data.Record.create([{
		name:'id',
		mapping:'ordersProduct.id'
	},{
		name: 'productId',
		mapping:'ordersProduct.productId'
	},{
		name: 'ordersId',
		mapping: 'ordersProduct.ordersId'
	},{
		name: 'productName',
		mapping:'product.name'
	},{
		name: 'priceSale',
		mapping:'product.priceSale'
	},{
		name: 'priceDiscount',
		mapping: 'ordersProduct.priceDiscount'
	},{
		name: 'quantity',
		mapping: 'ordersProduct.quantity'
	}])
});

caiban.easyadmin.orders.ProductCombo = Ext.extend(Ext.form.ComboBox,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var ds=new Ext.data.JsonStore({
			autoLoad:false,
			root:"records",
			url:Context.ROOT+"/orders/orders!productCombo.act",
			fields:[{name:"id",mapping:"product.id"},
			       {name:"name",mapping:"product.name"},
			       {name:"priceSale",mapping:"product.priceSale"},
			       {name:"productCategoryId",mapping:"product.productCategoryId"},
			       {name:"productCategoryName",mapping:"productCategory.name"}]
		});
		
		var resultTpl = new Ext.XTemplate(
	        '<tpl for="."><div class="search-item">',
	            '[{productCategoryName}]{name}',
	        '</div></tpl>'
	    );
		
		var c={
			store: ds,
			displayField:'name',
			typeAhead: false,
			pageSize: 0,
			minChars :0,
			hideTrigger:true,
	       tpl: resultTpl,
	       itemSelector: 'div.search-item'
		};
		
		caiban.easyadmin.orders.ProductCombo.superclass.constructor.call(this,c);
	}
});

//买家信息表单
caiban.easyadmin.orders.EshopBuyerCombo=Ext.extend(Ext.form.ComboBox,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var ds=new Ext.data.JsonStore({
			autoLoad:false,
			root:"records",
			url:Context.ROOT+"/orders/orders!eshopBuyerCombo.act",
			fields:["id","name","phone","email"]
		});
		
		var resultTpl = new Ext.XTemplate(
	        '<tpl for="."><div class="search-item">',
	            '{name}({phone})',
	        '</div></tpl>'
	    );
		
		var c={
			store: ds,
			displayField:'name',
			typeAhead: false,
			pageSize: 0,
			minChars :0,
			hideTrigger:true,
	       tpl: resultTpl,
	       itemSelector: 'div.search-item'
		};
		
		caiban.easyadmin.orders.EshopBuyerCombo.superclass.constructor.call(this,c);
	}
});

caiban.easyadmin.orders.DeliveryOrdersWin = function(ordersId,ordersNo){
	
	var form = new caiban.easyadmin.orders.LogisticsDetailsForm({
//		id:ORDERS.NEWORDERS_FORM,
		saveUrl:Context.ROOT+"/orders/orders!createLogisticsDetails.act",
		region:"center"
	});
	
	form.initForm(ordersId,ordersNo)
	
	var win = new Ext.Window({
		id:ORDERS.LOGISTICS_DETAILS_WIN,
		title:"订单发货",
		width:500,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	win.show();
}

//订单发货时填写的表单
caiban.easyadmin.orders.LogisticsDetailsForm=Ext.extend(Ext.form.FormPanel, {
	constructor:function(config){
		config=config||{};
		Ext.apply(this,config);
		var form=this;
		var c={
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
					id:"orderId",
					name:"logisticsDetails.orderId"
				},{
					xtype:"hidden",
					id:"logisticsId",
					name:"logisticsDetails.logisticsId"
				},{
					id:"orderNo",
					fieldLabel:"订单号",
					readOnly:true
				},new caiban.easyadmin.orders.LogisticsCombo({
					id:"logisticsName",
					name:"logisticsDetails.logisticsName",
					fieldLabel:"物流",
					onSelect:function(records){
						this.setValue(records.get("name"));
						form.findById("logisticsId").setValue(records.get("id"));
			        	this.collapse();
					}
				}),{
					id:"expressNo",
					name:"logisticsDetails.expressNo",
//					itemCls:"required",
//					allowBlank:false,
					fieldLabel:"发货单号"
				},{
					xtype:"numberfield",
					id:"costs",
					name:"logisticsDetails.costs",
					fieldLabel:"费用(元)",
					value:0
				},{
					xtype:"datefield",
					id:"gmtSend",
					name:"logisticsDetails.gmtSend",
					format: "Y-m-d",
					fieldLabel:"发货时间",
					value:new Date()
				}],
				buttons:[{
					text:"发货",
					handler:this.saveForm,
					scope:this
				},{
					text:"取消",
					scope:this,
					handler:function(btn){
						Ext.getCmp(ORDERS.LOGISTICS_DETAILS_WIN).close();
					}
				}]
		};
		
		caiban.easyadmin.orders.LogisticsDetailsForm.superclass.constructor.call(this,c);
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
	onSaveSuccess:function(_form,_action){
		Ext.getCmp(ORDERS.ORDERS_GRID).getStore().reload();
		Ext.getCmp(ORDERS.LOGISTICS_DETAILS_WIN).close();
	},
	onSaveFailure:function(_form,_action){
		Ext.MessageBox.show({
			title:MESSAGE.title,
			msg : "订单没有发货,请确认您的订单现在没有发过货!",
			buttons:Ext.MessageBox.OK,
			icon:Ext.MessageBox.ERROR
		});
	},
	initForm:function(ordersId,ordersNo){
		this.findById("orderId").setValue(ordersId);
		this.findById("orderNo").setValue(ordersNo);
	}
});

//买家信息表单
caiban.easyadmin.orders.LogisticsCombo=Ext.extend(Ext.form.ComboBox,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var ds=new Ext.data.JsonStore({
			autoLoad:false,
			root:"records",
			url:Context.ROOT+"/orders/orders!logisticsCombo.act",
			fields:["id","name"]
		});
		
		var resultTpl = new Ext.XTemplate(
	        '<tpl for="."><div class="search-item">',
	            '{name}',
	        '</div></tpl>'
	    );
		
		var c={
			store: ds,
			displayField:'name',
			typeAhead: false,
			pageSize: 0,
			minChars :0,
			hideTrigger:true,
	       tpl: resultTpl,
	       itemSelector: 'div.search-item'
		};
		
		caiban.easyadmin.orders.LogisticsCombo.superclass.constructor.call(this,c);
	}
});

caiban.easyadmin.orders.DealSuccess = function(ordersId,grid){
	Ext.Ajax.request({
		url:Context.ROOT+"/orders/orders!arrivalConfirm.act",
		params:{"logisticsDetails.orderId":ordersId},
		success:function(response,opt){
			var obj = Ext.decode(response.responseText);
			if(obj.success){
				grid.getStore().reload();
			}else{
				Ext.MessageBox.show({
					title:MESSAGE.title,
					msg : "交易没有被标记为成功,请检查订单状态!",
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
 * 创建新的售后纠纷表单
 * @param orderid:涉及纠纷的订单ID
 * @param orderno:涉及纠纷的订单号
 * */
caiban.easyadmin.orders.NewSaleDisputeWin=function(orderid,orderno){
	var form = new caiban.easyadmin.orders.saledispute.SaleDisputeForm({
		id:ORDERS.NEWSALEDISPUTE_FORM,
		region:"center",
		onSaveSuccess:function(_form,_action){
			Ext.getCmp(ORDERS.ORDERS_GRID).getStore().reload();
			Ext.getCmp(ORDERS.NEWSALEDISPUTE_WIN).close();
		},
		cancelSubmit:function(){
			Ext.getCmp(ORDERS.NEWSALEDISPUTE_WIN).close();
		}
	});
	
	form.initForm(orderid,orderno);
	
	var win = new Ext.Window({
		id:ORDERS.NEWSALEDISPUTE_WIN,
		title:"提交纠纷",
		width:600,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	
	win.show();
}

caiban.easyadmin.orders.changeOrdersStatus = function(item,checked){
	var menu = Ext.getCmp("statusMenu");
	var arr = new Array();
	for(var i=0;i<menu.items.length;i++){
		if(menu.items.itemAt(i).checked){
			arr.push(menu.items.itemAt(i).inputValue);
		}
	}
	
	var grid = Ext.getCmp(ORDERS.ORDERS_GRID);
	var B=grid.getStore().baseParams||{};
	B["keyArray"] = arr.join(",");
	grid.getStore().baseParams = B;
//	reload的时候与autoLoad一样,可以重新设置分页变量,对pagingToolBar的更新也需要此配置
	grid.getStore().reload({params:{"page.start":0,"page.limit":Context.PAGE_SIZE}});
}
