Ext.namespace("caiban.easyadmin.product");

/**
 * 权限管理中会用到的变量
 * */
var Const = new function(){
	this.CATEGORY_WIN = "categorywin";
	this.CATEGORY_TREE = "categorytree";
	this.PRODUCT_GRID="productgridpanel";
	this.PRODUCT_WIN = "productwin";
	this.PRODUCT_INFO_FORM = "productinfoform";
	this.IMAGE_TAB = "imagetab";
}

Ext.onReady(function(){

	var tree =new caiban.easyadmin.product.category.TreePanel({
		id:Const.CATEGORY_TREE,
		layout:"fit",
		region:"west"
	});
	
	var grid=new caiban.easyadmin.product.GridPanel({
		id:Const.PRODUCT_GRID,
		title:"产品信息库",
		layout:"fit",
		region:"center"
	});
	
	var viewport = new Ext.Viewport({
		layout:"border",
		items:[tree,grid]
	});
	
	tree.getRootNode().expand();
	tree.on("click",function(node,e){
		var _store = grid.getStore();
		var B=_store.baseParams;
		if(node.attributes.data>0){
			B["productCategory.id"]=node.attributes.data;
		}else{
			B["productCategory.id"]=null;
		}
		
//		if(grid.onlyShowByMe.getValue()){
//			B["product.uid"] = Ext.get("authuserid").dom.value;
//		}
		
		_store.baseParams=B;
		_store.reload({params:{"page.start":0,"page.limit":Context.PAGE_SIZE}});  
	});
}); 

caiban.easyadmin.product.GridPanel =Ext.extend(Ext.grid.GridPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var _fields = this.porductRecord;
		var _url = this.listUrl; 
		var _store = new Ext.data.JsonStore({
			root:"records",
			totalProperty:'totals',
			remoteSort:true,
			fields:_fields,
			url:_url,
			autoLoad:true
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
	                } else {
	                    grid.removeButton.disable();
	                    grid.editButton.disable();
	                }
	            }
	        }	
		});
		var _cm=new Ext.grid.ColumnModel([_sm,{
			header:'编号',
			width:10,
			sortable:true,
			dataIndex:'id',
			hidden:true
		},{
			header:'产品名称',
			sortable:false,
			dataIndex:'name'
		},{
			header:'类别',
			sortable:false,
			dataIndex:'categoryName'
		},{
			header:'产品型号',
			sortable:false,
			dataIndex:'model'
		},{
			header:'进货价格',
			sortable:false,
			dataIndex:'pricePurchase'
		},{
			header:'产品销售价格',
			sortable:false,
			dataIndex:'priceSale'
		},{
			header:'产品尺寸',
			sortable:false,
			dataIndex:'size'
		},{
			header:'产品描述',
			sortable:false,
			dataIndex:'remark'
		},{
			header:'发布人',
			sortable:false,
			dataIndex:'uid',
			hidden:true
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
		
		caiban.easyadmin.product.GridPanel.superclass.constructor.call(this,c);
	},
	porductRecord:Ext.data.Record.create([{
		name: 'id',
		mapping:'product.id',
		type: 'int'
	},{
		name: 'name',
		mapping:'product.name'
	},{
		name: 'categoryName',
		mapping:'productCategory.name'
	},{
		name: 'model',
		mapping:'product.model'
	},{
		name: 'pricePurchase',
		mapping:'product.pricePurchase'
	},{
		name: 'priceSale',
		mapping:'product.priceSale'
	},{
		name: 'size',
		mapping:'product.size'
	},{
		name: 'remark',
		mapping:'product.remark'
	},{
		name: 'uid',
		mapping:'product.uid'
	}]),
	listUrl:Context.ROOT+"/product/product!listProduct.act",
	mytoolbar:[{
		iconCls:"add16",
		text:"添加新产品",
		handler:function(btn){
			// TODO 添加新产品
			caiban.easyadmin.product.AddFormWin();
		}
	},{
		iconCls:"edit16",
		text:"编辑产品",
		disabled:true,
		ref:"../editButton",
		handler:function(btn){
			// TODO 编辑产品信息
			var productGrid = Ext.getCmp(Const.PRODUCT_GRID);
			var selectedRecord = productGrid.getSelectionModel().getSelected();
			if(typeof(selectedRecord) == "undefined"){
				Ext.MessageBox.show({
					title:MESSAGE.title,
					msg : MESSAGE.needOneRecord,
					buttons:Ext.MessageBox.OK,
					icon:Ext.MessageBox.WARNING
				});
				return ;
			}
			caiban.easyadmin.product.EditFormWin(selectedRecord.get("id"));
		}
	},{
		iconCls:"delete16",
		text:"删除产品信息",
		ref:"../removeButton",
		disabled: true,
		handler:function(btn){
			// TODO 删除产品信息
			Ext.MessageBox.confirm(MESSAGE.title,MESSAGE.confirmDelete,function(btn){
				if(btn!="yes"){
					return ;
				}
				
				var grid = Ext.getCmp(Const.PRODUCT_GRID);
				var row = grid.getSelectionModel().getSelections();
				var _ids = new Array();
				for (var i=0,len = row.length;i<len;i++){
					var _id=row[i].get("id");
					_ids.push(_id);
				}
				
				Ext.Ajax.request({
					url:Context.ROOT+"/product/product!batchDeleteProduct.act",
					params:{"productArrays":_ids.join(",")},
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
	},"->",{
		xtype:"checkbox",
		boxLabel:"只显示我添加的",
		ref:"../onlyShowByMe",
		scope:this,
		handler:function(btn){
			var _store = Ext.getCmp(Const.PRODUCT_GRID).getStore();
			var B=_store.baseParams||{};
			
			if(btn.getValue()){
				B["product.uid"]=Ext.get("authuserid").dom.value;
			}else{
				B["product.uid"]=null;
			}
			
//			var tree=Ext.getCmp(Const.CATEGORY_TREE);
//			var node = tree.getSelectionModel().getSelectedNode();
//			
//			if(node!=null && node.attributes.data>0){
//				B["productCategory.id"]=node.attributes.data;
//			}
			
			_store.baseParams = B;
			//reload的时候与autoLoad一样,可以重新设置分页变量,对pagingToolBar的更新也需要此配置
			_store.reload({params:{"page.start":0,"page.limit":Context.PAGE_SIZE}});  
		}
	}]
});

caiban.easyadmin.product.InfoForm = Ext.extend(Ext.form.FormPanel, {
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);

		var c={
			labelAlign : "right",
			labelWidth : 60,
			layout:"column",
			bodyStyle:'padding:5px 0 0',
			frame:true,
			items:[{
				columnWidth:1,
				layout:"form",
				defaults:{
					anchor:"95%",
					xtype:"textfield",
					labelSeparator:""
				},
				items:[{
					xtype:"hidden",
					id:"id",
					name:"product.id"
				},{
					id:"name",
					name:"product.name",
					fieldLabel:"产品名称",
					allowBlank:false,
					itemCls:"required"
				}]
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
					xtype:"combotree",
					fieldLabel:"产品类别",
					allowBlank:false,
					itemCls:"required",
					id:"productCategoryName",
					name:"productCategory.name",
					hiddenId:"productCategoryId",
					hiddenName:"product.productCategoryId",
					tree:new Ext.tree.TreePanel({
						loader: new Ext.tree.TreeLoader({
							url:Context.ROOT+'/product/product!listProductCategoryByParent.act',
							listeners:{
								beforeload:function(treeload,node){
									this.baseParams["productCategory.parentId"] = node.attributes.data;
								}
							}
						}),
				   	 	root : new Ext.tree.AsyncTreeNode({text:'所有分类',data:0})
					})
				},
				{
					id:"model",
					name:"product.model",
					fieldLabel:"产品型号"
				},{
					xtype:"numberfield",
					id:"pricePurchase",
					name:"product.pricePurchase",
					fieldLabel:"进货价格"
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
					id:"unit",
					name:"product.unit",
					fieldLabel:"单位"
				},{
					id:"size",
					name:"product.size",
					fieldLabel:"产品尺寸"
				},{
					xtype:"numberfield",
					id:"priceSale",
					name:"product.priceSale",
					fieldLabel:"销售价格"
				}]
			},{
				columnWidth:1,
				layout:"form",
				defaults:{
					anchor:"95%",
					xtype:"textfield",
					labelSeparator:""
				},
				items:[{
					xtype:"textarea",
					id:"remark",
					name:"product.remark",
					fieldLabel:"产品描述"
				}]
			},{
				xtype:"tabpanel",
				id:Const.IMAGE_TAB,
				columnWidth:1,
				activeTab:0,
				border:false,
				defaults:{
					height:200
				},
				items:[
					new caiban.easyadmin.product.image.ImageView({
						title:"产品图片"
					}),
					new caiban.easyadmin.product.stock.BaseGridView({
						title:"产品库存"
					})
				]
			}],
			buttons:[{
				text:"保存",
				handler:this.saveForm,
				scope:this
			},{
				text:"关闭",
				handler:function(){
					// TODO 关闭编辑产品的窗口
//					Ext.getCmp(Const.PRODUCT_WIN).hide();
					Ext.getCmp(Const.PRODUCT_WIN).close();
				}
			}]
		};
		
		caiban.easyadmin.product.InfoForm.superclass.constructor.call(this,c);
	},
	saveUrl:Context.ROOT+"/product/product!createProduct.act",
	saveForm:function(){
		// TODO 保存产品信息
		//Step1.保存基本信息
		//Step2.保存图片信息
		var _url = this.saveUrl;
		if(this.getForm().isValid()){
			this.getForm().submit({
				url:_url,
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
	mystore:null,
	loadProduct:function(id){
		
		var _fields = [{
			name: 'product.id',
			type:'int'
		},{
			name: 'product.productCategoryId',
			type:'int'
		},{
			name: 'product.name',
			type:"string"
		},{
			name: 'product.pricePurchase',
			type:"float"
		},{
			name: 'product.priceSale',
			type:"float"
		},{
			name: 'product.model',
			type:"string"
		},{
			name: 'product.size',
			type:"string"
		},{
			name: 'product.remark',
			type:"string"
		},{
			name: 'product.unit',
			type:"string"
		},{
			name: 'productCategory.name',
			type:"string"
		}]
		var form = this;
		var store=new Ext.data.JsonStore({
			root : "records",
			fields : _fields,
			url : Context.ROOT + "/product/product!listOneProduct.act",
			baseParams:{"product.id":id},
			autoLoad : true
			,
			listeners : {
//				"exception":function(misc){
//					alert(misc+"   "+store.getCount())
//				},
				"datachanged" : function(s) {
					var record = s.getAt(0);
					if (record == null) {
						Ext.MessageBox.alert(MESSAGE.title,MESSAGE.loadError);
					} else {
						form.getForm().loadRecord(record);
						form.findById("productCategoryName").setValue({text:record.get("productCategory.name"),attributes:{data:record.get("product.productCategoryId")}});
					}
				}
			}
		})
		
	},
	loadImage:function(id){
		// TODO 载入图片信息
		var imageview = this.findById(Const.IMAGE_TAB);
		if(id>0){
			imageview.get(0).imageStore.load({
				params:{"product.id":id}
			});
			imageview.get(0).uploadImageButton.enable();
			imageview.get(0).deleteImageButton.enable();
		}else{
			imageview.get(0).imageStore.removeAll();
			imageview.get(0).uploadImageButton.disable();
			imageview.get(0).deleteImageButton.disable();
		}
	},
	loadStock:function(id){
		// TODO 载入库存信息
	},
	onSaveSuccess:function(_form,_action){
		Ext.getCmp(Const.PRODUCT_GRID).getStore().reload();
//		Ext.getCmp(Const.PRODUCT_WIN).hide();
		Ext.getCmp(Const.PRODUCT_WIN).close();
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

//caiban.easyadmin.product.AddFormWin = function(){
//
//	var win = Ext.getCmp(Const.PRODUCT_WIN );
//	var form;
//	if(!win){
//		 form = new caiban.easyadmin.product.InfoForm({
//			id:Const.PRODUCT_INFO_FORM,
//			region:"center"
//		});
//		win = new Ext.Window({
//			id:Const.PRODUCT_WIN ,
//			closeAction:"hide",
//			title:"添加产品信息",
//			width:"80%",
//			autoHeight:true,
//			modal:true,
//			items:[form]
//		});
//	}else{
//		form = Ext.getCmp(Const.PRODUCT_INFO_FORM);
//		form.saveUrl=Context.ROOT+"/product/product!createProduct.act";
//		form.getForm().reset();
//		form.findById("productCategoryName").setValue({text:"",attributes:{data:"0"}})
//	}
//	form.loadImage(0);
//	win.show();
//}

//caiban.easyadmin.product.EditFormWin = function(id){
//	var form;
//	var win = Ext.getCmp(Const.PRODUCT_WIN );
//	if(!win){
//		form = new caiban.easyadmin.product.InfoForm({
//			id:Const.PRODUCT_INFO_FORM,
//			region:"center",
//			saveUrl:Context.ROOT+"/product/product!updateProduct.act"
//		});
//		
//		win = new Ext.Window({
//			id:Const.PRODUCT_WIN ,
//			closeAction:"hide",
//			title:"编辑产品信息",
//			width:"80%",
//			autoHeight:true,
//			modal:true,
//			items:[form]
//		});
//	}else{
//		form = Ext.getCmp(Const.PRODUCT_INFO_FORM);
//		form.saveUrl=Context.ROOT+"/product/product!updateProduct.act";
////		form.loadStock(id);
//	}
//	form.loadProduct(id);
//	form.loadImage(id);
//	win.show();
//}

caiban.easyadmin.product.AddFormWin = function(){
//	var win = Ext.getCmp(Const.PRODUCT_WIN );
//	var form;
//	if(!win){
	var form = new caiban.easyadmin.product.InfoForm({
		id:Const.PRODUCT_INFO_FORM,
		region:"center"
	});
	
	var win = new Ext.Window({
		id:Const.PRODUCT_WIN ,
//			closeAction:"hide",
		title:"添加产品信息",
		width:"80%",
		autoHeight:true,
		modal:true,
		maximizable:true,
		items:[form]
	});
//	}else{
//		form = Ext.getCmp(Const.PRODUCT_INFO_FORM);
//		form.saveUrl=Context.ROOT+"/product/product!createProduct.act";
//		form.getForm().reset();
//		form.findById("productCategoryName").setValue({text:"",attributes:{data:"0"}})
//	}
//	form.loadImage(0);
	win.show();
}

caiban.easyadmin.product.EditFormWin = function(id){
//	var form;
//	var win = Ext.getCmp(Const.PRODUCT_WIN );
//	if(!win){
	var form = new caiban.easyadmin.product.InfoForm({
		id:Const.PRODUCT_INFO_FORM,
		region:"center",
		saveUrl:Context.ROOT+"/product/product!updateProduct.act"
	});
		
	var win = new Ext.Window({
		id:Const.PRODUCT_WIN ,
//			closeAction:"hide",
		title:"编辑产品信息",
		width:"80%",
		autoHeight:true,
		modal:true,
		maximizable:true,
		items:[form]
	});
//	}else{
//		form = Ext.getCmp(Const.PRODUCT_INFO_FORM);
//		form.saveUrl=Context.ROOT+"/product/product!updateProduct.act";
////		form.loadStock(id);
//	}
	form.loadProduct(id);
	form.loadImage(id);
	win.show();
}

