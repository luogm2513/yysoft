//pageSize必需,totalRecords:必需,startIndex必需,contenter必需
function pageNavBar(config){
	//alert("pagesize:"+config.pageSize+"  total:"+config.totalRecords+"  unknowsss:"+config.startIndex);
	var lastPage=0;
	var startIndex=0;
	var thisPage=1;
	var template={firstPageLabel:"<<",previousPageLabel:"<",nextPageLabel:">",lastPageLabel:">>"};
	var pfx=typeof(config.pfx)!="undefined"?config.pfx:"";
	var param=typeof(config.param)!="undefined"?"&"+config.param:"";
	
	if(typeof(config.template)!="undefined"){
		template=config.template;
	}
	var totalPage=parseInt((config.totalRecords-1)/config.pageSize)+1;
	totalPage=totalPage<1?1:totalPage;
	
	var navStr="";
	
	navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"start=0"+param+"' >"+template.firstPageLabel+"</a>";  //首页
	startIndex=(config.startIndex-config.pageSize)<=0?0:(config.startIndex-config.pageSize);  
	navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"start="+startIndex+param+"' >"+template.previousPageLabel+"</a>";//上一页
	
	thisPage=parseInt(config.startIndex/15)+1;
	
	for(var i=-5;i<6;i++){
		if((thisPage+i)>=0 && (thisPage+i)<totalPage){
			startIndex=(thisPage+i)*config.pageSize;
			if(startIndex==config.startIndex){
				navStr=navStr+"&nbsp;<a href='#' class='click'>"+(thisPage+i+1)+"</a>";
			}else{
				navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"startIndex="+startIndex+param+"' >"+(thisPage+i+1)+"</a>";
			}
		}
	}
	
	startIndex=(config.startIndex+config.pageSize)>=config.totalRecords?(totalPage-1)*config.pageSize:config.startIndex+config.pageSize;  
	navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"start="+startIndex+param+"' >"+template.nextPageLabel+"</a>";//下一页
	startIndex=(totalPage-1)*config.pageSize;
	navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"start="+startIndex+param+"' >"+template.lastPageLabel+"</a>";//末页
	if(config.contenter.constructor==Array){
	     $(config.contenter).each(function(){
	         $("#"+this).html(navStr);
	     });
	}else{
        $("#"+config.contenter).html(navStr);
	}
}

function pageNavBarByForm(config){
	//alert("pagesize:"+config.pageSize+"  total:"+config.totalRecords+"  unknowsss:"+config.startIndex);
	var lastPage=0;
	var startIndex=0;
	var thisPage=1;
	var template={firstPageLabel:"<<",previousPageLabel:"<",nextPageLabel:">",lastPageLabel:">>"};
	var pfx=typeof(config.pfx)!="undefined"?config.pfx:"";
	var param=typeof(config.param)!="undefined"?"&"+config.param:"";
	
	if(typeof(config.template)!="undefined"){
		template=config.template;
	}
	var totalPage=parseInt((config.totalRecords-1)/config.pageSize)+1;
	totalPage=totalPage<1?1:totalPage;
	
	var navStr="";
	
	navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"start=0"+param+"' >"+template.firstPageLabel+"</a>";  //首页
	startIndex=(config.startIndex-config.pageSize)<=0?0:(config.startIndex-config.pageSize);  
	navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"start="+startIndex+param+"' >"+template.previousPageLabel+"</a>";//上一页
	
	thisPage=parseInt(config.startIndex/15)+1;
	
	for(var i=-5;i<6;i++){
		if((thisPage+i)>=0 && (thisPage+i)<totalPage){
			startIndex=(thisPage+i)*config.pageSize;
			if(startIndex==config.startIndex){
				navStr=navStr+"&nbsp;<a href='#' class='click'>"+(thisPage+i+1)+"</a>";
			}else{
				navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"startIndex="+startIndex+param+"' >"+(thisPage+i+1)+"</a>";
			}
		}
	}
	
	startIndex=(config.startIndex+config.pageSize)>=config.totalRecords?(totalPage-1)*config.pageSize:config.startIndex+config.pageSize;  
	navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"start="+startIndex+param+"' >"+template.nextPageLabel+"</a>";//下一页
	startIndex=(totalPage-1)*config.pageSize;
	navStr=navStr+"&nbsp;<a href='"+config.url+"?"+pfx+"start="+startIndex+param+"' >"+template.lastPageLabel+"</a>";//末页
	if(config.contenter.constructor==Array){
	     $(config.contenter).each(function(){
	         $("#"+this).html(navStr);
	     });
	}else{
        $("#"+config.contenter).html(navStr);
	}
}