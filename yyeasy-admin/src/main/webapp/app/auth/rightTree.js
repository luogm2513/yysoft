Ext.namespace('caiban.easyadmin.auth.right');

caiban.easyadmin.auth.right.RightTree = function (config){
	config = config||{};
	
	var _id=config.id||"rightTree";
	var _region = config.region||"center";
	var treeLoad = new Ext.tree.TreeLoader({url:Context.ROOT+'/auth/right!listRightChild.act'});
	var tree = new Ext.tree.TreePanel({
		id:_id,
		layout:'fit',
		region:_region,
		width:200,
		rootVisible:true,
		autoScroll:true,
		animate:true,
		split:true,
		root:{
			nodeType:'async',
			text:'所有权限',
			data:0
		},
		loader:treeLoad,
		listeners: {
            'click': function(node, checked){
            }
        },
        tbar:[
        	{text:"全部展开",handler:function(){
        		tree.expandAll();
        	}},
        	{text:"全部折叠",handler:function(){
        		tree.collapseAll();
        	}}
        ]
	});
	
	treeLoad.on("beforeload",function(treeload,node){
		this.baseParams["right.parentId"] = node.attributes.data;
	});
//	
	tree.on("contextmenu",function(node,event){
		if(config.contextmenu!=null){
	    	event.preventDefault();
	    	config.contextmenu.showAt(event.getXY());
	    	node.select();
		}
    });
	
	tree.getRootNode().expand();
	
	return tree;
}