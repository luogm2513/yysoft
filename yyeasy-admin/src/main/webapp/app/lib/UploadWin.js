Ext.namespace("caiban.easyadmin");
caiban.easyadmin.UploadConfig = new function(){
	this.uploadURL = Context.ROOT+"/upload";
	this.uploadSuccess = null;
}
caiban.easyadmin.UploadWin = Ext.extend(Ext.Window, {
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var c={
			width:380,
			autoHeight:true,
			modal:true,
			items:[new Ext.FormPanel({
				id:"fileuploadform",
				fileUpload: true,
				labelAlign : "right",
				labelWidth : 60,
				layout:"form",
				bodyStyle:'padding:5px 0 0',
				frame:true,
				items:[{
		            xtype: 'fileuploadfield',
		            id: 'form-file',
		            allowBlank:false,
		            emptyText: '请选择一个文件',
		            fieldLabel: '选择文件',
		            itemCls:"required",
		            name: 'doc',
		            anchor:"95%",
		            buttonText: '',
		            buttonCfg: {
		                iconCls: 'upload-icon'
		            }
		        }],
		        buttons:[{
		        	text:"上传",
		        	socpe:this,
		        	handler:function(btn){
		        		var fm = Ext.getCmp("fileuploadform");
		        		if(fm.getForm().isValid()){
			                fm.getForm().submit({
			                    url: caiban.easyadmin.UploadConfig.uploadURL,
			                    waitMsg: MESSAGE.uploading,
//			                    waitTitle: MESSAGE.uploading,
			                    success: caiban.easyadmin.UploadConfig.uploadSuccess
//			                    success:function(f,o){
//			                    	alert(o.result.success)
//			                    }
			                });
		                }
		        	}
		        }]
			})]
		};
		
		caiban.easyadmin.UploadWin.superclass.constructor.call(this,c);
	}
});