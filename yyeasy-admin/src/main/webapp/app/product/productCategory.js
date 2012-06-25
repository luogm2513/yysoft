Ext.namespace("caiban.easyadmin.product.category");

caiban.easyadmin.product.category.TreePanel = Ext.extend(Ext.tree.TreePanel,{
	constructor:function(config){
		config = config || {};
		Ext.apply(this,config);
		
		var treeLoad = new Ext.tree.TreeLoader({
			url:Context.ROOT+'/product/product!listProductCategoryByParent.act',
			listeners:{
				beforeload:function(treeload,node){
					this.baseParams["productCategory.parentId"] = node.attributes.data;
				}
			}
		});
		
		var c={
			width:220,
			rootVisible:true,
			autoScroll:true,
			animate:true,
			split:true,
			root:{
				nodeType:'async',
				text:'所有类别',
				data:0
			},
			loader:treeLoad,
			tbar:[
	        	{
	        		text:"全部展开",
	        		scope:this,
	        		handler:function(){
	        			this.expandAll();
	        		}
	        	},{
	        		text:"全部折叠",
	        		scope:this,
	        		handler:function(){
	        			this.collapseAll();
	        		}
	        	}
	        ],
	        contextMenu:this.contextmenu,
	        listeners:{
	        	contextmenu:function(node,e){
	        		node.select();
	        		var c = node.getOwnerTree().contextMenu;
		            c.contextNode = node;
		            c.showAt(e.getXY());
	        	}
	        }
		};
		
		caiban.easyadmin.product.category.TreePanel.superclass.constructor.call(this,c);
		
	},
	contextmenu:new Ext.menu.Menu({
		items: [{
			id: 'cm-add',
			cls:'add16',
			text: '增加类别',
			handler:function(btn){
				caiban.easyadmin.product.category.AddCategoryWin();
            }
        },{
            id: 'cm-edit',
            cls:'edit16',
            text: '修改类别',
            handler:function(btn){
            	if(btn.parentMenu.contextNode.attributes.data>0){
            		caiban.easyadmin.product.category.EditCategoryWin(btn.parentMenu.contextNode.attributes.data);
            	}
            }
        },{
            id: 'cm-del',
            cls:'delete16',
            text: '删除类别',
            handler:function(btn){
            	if(btn.parentMenu.contextNode.attributes.data>0){
            		// TODO 删除类别
            		var tree = Ext.getCmp(Const.CATEGORY_TREE);
					var node = tree.getSelectionModel().getSelectedNode();
//					if(tree.getRootNode() == node){
//						return ;
//					}
					Ext.MessageBox.confirm(MESSAGE.title,MESSAGE.confirmDelete, function(btn){
						if(btn != "yes"){
							return false;
						}
						//从服务端删除权限节点
						Ext.Ajax.request({
							url:Context.ROOT+"/product/product!deleteProductCategory.act",
							params:{"productCategory.id":node.attributes.data},
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
            }
        }]
	})
});

caiban.easyadmin.product.category.InfoForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
		config=config || {};
		Ext.apply(this,config);
		
		var c={
			abelAlign : "right",
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
				id:"productCategory.id",
				name:"productCategory.id"
			},{
				xtype:"hidden",
				id:"parentId",
				name:"productCategory.parentId"
			},{
				xtype:"textfield",
				id:"parent",
				readOnly:true,
				fieldLabel:"父类别"
			},{
				xtype:"textfield",
				id:"productCategory.name",
				name:"productCategory.name",
				allowBlank:false,
				fieldLabel:"类别名称"
			}],
			buttons:[{
				text:"保存",
				handler:this.saveForm,
				scope:this
			},{
				text:"关闭",
				handler:function(){
					Ext.getCmp(Const.CATEGORY_WIN).close();
				}
			}]
		};
		
		caiban.easyadmin.product.category.InfoForm.superclass.constructor.call(this,c);
	},
	saveUrl:Context.ROOT+"/product/product!createProductCategory.act",
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
		var tree = Ext.getCmp(Const.CATEGORY_TREE);
		var node = tree.getSelectionModel().getSelectedNode();
		if(this.findById("productCategory.id").getValue() > 0){
			node.setText(this.findById("productCategory.name").getValue()); 
		}else{
			node.leaf= false;
			tree.getLoader().load(node,function(){
				node.expand();
			});
		}
		Ext.getCmp(Const.CATEGORY_WIN).close();
	},
	onSaveFailure:function(_form,_action){
		Ext.MessageBox.show({
			title:MESSAGE.title,
			msg : MESSAGE.saveFailure,
			buttons:Ext.MessageBox.OK,
			icon:Ext.MessageBox.ERROR
		});
	},
	loadCategory:function(){
		var tree = Ext.getCmp(Const.CATEGORY_TREE);
		var node = tree.getSelectionModel().getSelectedNode();
		
		var _fields = [{
			name : "productCategory.id",
			mapping : "id"
		}, {
			name : "productCategory.name",
			mapping : "name"
		}];
		
		var form = this;
		var _store = new Ext.data.JsonStore({
			root : "records",
			fields : _fields,
			url : Context.ROOT + "/product/product!listOneProductCategoryById.act?productCategory.id="+node.attributes.data,
			autoLoad : true,
			listeners : {
				"datachanged" : function() {
					var record = _store.getAt(0);
					if (record == null) {
						Ext.MessageBox.alert(MESSAGE.title,MESSAGE.loadError);
						Ext.getCmp(Const.CATEGORY_WIN).close();
					} else {
						form.getForm().loadRecord(record);
					}
				}
			}
		});
		
	},
	initParentForAdd:function(){
		var tree = Ext.getCmp(Const.CATEGORY_TREE);
		var node = tree.getSelectionModel().getSelectedNode();
		this.findById("parentId").setValue(node.attributes.data);
		this.findById("parent").setValue(node.text);
	},
	initParentForUpdate:function(){
		var tree = Ext.getCmp(Const.CATEGORY_TREE);
		var node = tree.getSelectionModel().getSelectedNode();
		this.findById("parent").setValue(node.parentNode.text);
	}
});

caiban.easyadmin.product.category.AddCategoryWin = function (){
	var form = new caiban.easyadmin.product.category.InfoForm();
	form.saveUrl = Context.ROOT+"/product/product!createProductCategory.act";
	form.initParentForAdd();
	
	var win = new Ext.Window({
		id:Const.CATEGORY_WIN,
		title:"添加产品类别",
		width:380,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	win.show();
}

caiban.easyadmin.product.category.EditCategoryWin = function (){
	var tree = Ext.getCmp(Const.CATEGORY_TREE);
	var node = tree.getSelectionModel().getSelectedNode();
	if(tree.getRootNode() == node){
		return false;
	}
	var form = new caiban.easyadmin.product.category.InfoForm();
	form.saveUrl = Context.ROOT+"/product/product!updateProductCategory.act";
	form.initParentForUpdate();
	form.loadCategory();
	var win = new Ext.Window({
		id:Const.CATEGORY_WIN,
		title:"修改产品类别",
		width:380,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	win.show();
}

caiban.easyadmin.product.category.ComboTree = Ext.extend(Ext.form.ComboBox,{
	treeId:Ext.id()+"-tree",
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var c={
			store:new Ext.data.SimpleStore({fields:[],data:[[]]}),
			mode:"local",
			triggerAction:"all",
			onSelect: Ext.emptyFn,
			editable:false,
			tpl: new Ext.Template('<tpl for="."><div style="height:250px"><div id="'+this.treeId+'"></div></div></tpl>'),
			listeners:{
		        'expand': function(combo){
		         	if(this.tree.rendered){
		         		return ;
		         	}
		         	Ext.apply(this.tree,{border:false, autoScroll:true});
		         	this.tree.render(this.treeId)
		         }
		    }
		};
		
		caiban.easyadmin.product.category.ComboTree.superclass.constructor.call(this,c);
		
	},
//	tree:null
	tree:new Ext.tree.TreePanel({
//		el:this.treeId,
//		xtype:'treepanel',
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
});
