Ext.namespace("caiban.easyadmin.product.image");

caiban.easyadmin.product.image.ImageView = Ext.extend(Ext.Panel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var _imgStore = this.imageStore;
		var _xtpl = this.xtpl;
		var _imgRecord = this.imageRecord;
		var c={
			id:'images-view',
			autoScroll:true,
			items:new Ext.DataView({
				store:_imgStore,
				tpl:_xtpl,
				multiSelect:true,
				overClass:'x-view-over',
				itemSelector:'div.thumb-wrap',
				emptyText:MESSAGE.noRecord
			}),
			tbar:[{
				iconCls:"add16",
				text:"添加新图片",
				disabled:true,
				ref:"../uploadImageButton",
				handler:function(btn){
					caiban.easyadmin.UploadConfig.uploadURL = Context.ROOT+"/product/product!uploadImage.act?product.id="+Ext.get("id").dom.value;
					var win = new caiban.easyadmin.UploadWin({
						title:"上传产品图片"
//						closeAction:"hide"
					});
					win.show();
					
					caiban.easyadmin.UploadConfig.uploadSuccess = function(f,o){
						// TODO 上传成功后的操作
						if(o.result.success){
							//获取返回的文件名和路径
							var pid=Ext.get("id").dom.value;
							if(pid>0){
							//如果是修改操作,重新载入store即可
								_imgStore.reload();
							}else{
							//如果是添加操作,则需要往store里添加一条record
								var imgnewrecord = new _imgRecord({
									filename:o.result.data.filename,
									filepath:o.result.data.filepath,
									filetype:o.result.data.filetype,
									remark:o.result.data.remark
								});
								
								_imgStore.add(imgnewrecord);
							}
							win.close();
						}else{
							Ext.MessageBox.show({
								title:MESSAGE.title,
								msg : getMsg(MESSAGE.uploadFailure, [o.result.data]),
								buttons:Ext.MessageBox.OK,
								icon:Ext.MessageBox.ERROR
							});
						}
					}
					
				}
			},{
				iconCls:"delete16",
				text:"删除选中图片",
				ref:"../deleteImageButton",
				disabled:true,
				scope:this,
				handler:function(btn){
					var imgview = this.get(0);
					
					Ext.MessageBox.confirm(MESSAGE.title,MESSAGE.confirmDelete,function(btn){
						if(btn!="yes"){
							return ;
						}
						var selectedrecords = imgview.getSelectedRecords()
						var _ids = new Array();
						for (var i=0,len = selectedrecords.length;i<len;i++){
							var _id=selectedrecords[i].get("id");
							_ids.push(_id);
						}
						
						Ext.Ajax.request({
							url:Context.ROOT+"/product/product!deleteProductImage.act",
							params:{"imageArrays":_ids.join(",")},
							success:function(response,opt){
								var obj = Ext.decode(response.responseText);
								if(obj.success){
									imgview.getStore().reload();
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
		};
		
		caiban.easyadmin.product.image.ImageView.superclass.constructor.call(this,c);

		this.get(0).on("dblclick",function(view,index,node,e){
//			var viewstore=view.getStore();
//			alert(Ext.get(node).child(".thumb img").dom.src)
			window.open(Ext.get(node).child(".thumb img").dom.src)
		});
	},
	porductId:0,
	//xtemplate,图片展现模板
	xtpl:new Ext.XTemplate('<tpl for=".">',
        '<div class="thumb-wrap" id="img-{id}">',
        '<div class="thumb"><img src="'+Context.ROOT_IMG+'{filepath}{filename}" title="{name}"></div>',
        '<span>{remark}</span></div>',
	    '</tpl>',
	    '<div class="x-clear"></div>'
	),
	imageStore:new Ext.data.JsonStore({
	    url: Context.ROOT+'/product/product!listImageByProduct.act',
	    scope:this,
//	    params:{"product.id":this.productId},
	    root: 'records',
	    fields: [
	        'id','productId','filename','filetype','filepath','remark',
	        {name:'gmtcreate', type:'date', dateFormat:'timestamp'}
	    ]
	}),
	imageRecord:Ext.data.Record.create([{
		name:"id",mapping:"id"
	},{
		name:"filename",mapping:"filename"
	},{
		name:"filepath",mapping:"filepath"
	},{
		name:"filetype",mapping:"filetype"
	},{
		name:"remark",mapping:"remark"
	},{
		name:"productId",mapping:"productId"
	},{
		name:"eid",mapping:"eid"
	},{
		name:"uid",mapping:"uid	"
	}])
})
