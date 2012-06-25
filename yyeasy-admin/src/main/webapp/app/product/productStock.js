Ext.namespace("caiban.easyadmin.product.stock");

/**
 * 产品库存信息
 * */
caiban.easyadmin.product.stock.BaseGridView=Ext.extend(Ext.Panel,{
	constructor:function(config){
		config=config||{};
		Ext.apply(this,config);
		
		var c={
		};
		
		caiban.easyadmin.product.stock.BaseGridView.superclass.constructor.call(this,c);
	}
});

