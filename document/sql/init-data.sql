
--update 2010.5.23
insert into param_type(type,names,gmtcreate) values('orders_payment','����֧����ʽ',now());
insert into param_type(type,names,gmtcreate) values('orders_status','����״̬',now());

insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','alipay','֧����',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','bank','��������',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','remittance','�ʾֻ��',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','cash','�ֽ�',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_payment','','orther','����',0,0,now());

insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','0','�¶���',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','10','����',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','20','�ѷ���',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','30','�ۺ����',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('orders_status','','40','�������',0,0,now());

--update 2010.5.15
insert into param_type(type,names,gmtcreate) values('combo_eshop_buyer_valuable','�ͻ��ȼ�',now());

insert into param(type,names,`key`,value,sort,used,gmtcreate) values('combo_eshop_buyer_valuable','','0','Ǳ�ڿͻ�',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('combo_eshop_buyer_valuable','','1','һ��ͻ�',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('combo_eshop_buyer_valuable','','2','��Ҫ�ͻ�',0,0,now());
insert into param(type,names,`key`,value,sort,used,gmtcreate) values('combo_eshop_buyer_valuable','','3','���׿ͻ�',0,0,now());

--update 2010.8.28 
insert into param_type (`type`,names,gmtcreate) values ('sale_dispute_severity','�������س̶�',now());
insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',0,'��΢',now());
insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',10,'һ��',now());
insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',20,'����',now());
insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',30,'����',now());

--update 2010.9.4
insert into param_type(`type`,names,gmtcreate) values('sale_dispute_resolved','���״���״̬',now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','0','�½�',0,0,now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','10','������',0,0,now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','20','�ѽ��',0,0,now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','30','�ѱչ�',0,0,now());
insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','40','�Ѿܾ�',0,0,now());