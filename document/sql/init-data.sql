
--update 2010.5.23
insert into param_type(type,names,gmtcreate) values('orders_payment','订单支付方式',now());
insert into param_type(type,names,gmtcreate) values('orders_status','订单状态',now());

insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','alipay','支付宝',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','bank','网上银行',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','remittance','邮局汇款',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','cash','现金',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','orther','其他',0,0,now());

insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','0','新订单',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','10','备货',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','20','已发货',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','30','售后纠纷',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','40','交易完成',0,0,now());

--update 2010.5.15
insert into param_type(type,names,gmtcreate) values('combo_eshop_buyer_valuable','客户等级',now());

insert into param(type,names,`key`,value,sort,used,gmtcreate) values('combo_eshop_buyer_valuable','','0','潜在客户',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('combo_eshop_buyer_valuable','','1','一般客户',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('combo_eshop_buyer_valuable','','2','重要客户',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('combo_eshop_buyer_valuable','','3','纠纷客户',0,0,now());

--update 2010.8.28 
insert into param_type (`type`,names,gmtcreate) values ('sale_dispute_severity','纠纷严重程度',now());
insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',0,'轻微',now());
insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',10,'一般',now());
insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',20,'严重',now());
insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',30,'紧急',now());

--update 2010.9.4
insert into param_type(`type`,names,gmtcreate) values('sale_dispute_resolved','纠纷处理状态',now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','0','新建',0,0,now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','10','进行中',0,0,now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','20','已解决',0,0,now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','30','已闭关',0,0,now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','40','已拒绝',0,0,now());