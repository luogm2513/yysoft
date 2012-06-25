/*==============================================================*/
/* Table: eshop                                                 */
/*==============================================================*/
create table eshop
(
   id                   int(20) not null auto_increment,
   name                 varchar(50),
   site_url             varchar(254),
   remark               varchar(254),
   domain               varchar(254),
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: eshop_account                                         */
/*==============================================================*/
create table eshop_account
(
   id                   int(20) not null auto_increment,
   eid                  int(20),
   uid                  int(20),
   account              varchar(50) comment '��ӦȨ��ϵͳ���˻�����uid��',
   name                 varchar(20),
   blocked              tinyint,
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: eshop_buyer                                           */
/*==============================================================*/
create table eshop_buyer
(
   id                   int(20) not null auto_increment,
   name                 varchar(20),
   phone                varchar(20),
   email                varchar(50),
   im                   varchar(50),
   valuable             tinyint default 0 comment '���Ҷ���ҵ�һ����������',
   remark               varchar(254),
   uid                  int(20),
   eid                  int(20),
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;


/*==============================================================*/
/* Table: logistics                                             */
/*==============================================================*/
create table logistics
(
   id                   int(20) not null auto_increment,
   name                 varchar(50),
   phone                varchar(20),
   address              varchar(254),
   routes               varchar(254),
   arrival_area         varchar(50),
   remark               varchar(254),
   uid                  int(20),
   eid                  int(20),
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: logistics_details                                     */
/*==============================================================*/
create table logistics_details
(
   id                   int(20) not null auto_increment,
   order_id             int(20),
   logistics_id         int(20),
   express_no           varchar(50),
   costs                float comment '����ʱ���������ã�����Ƶ��ɱ�����',
   gmt_receive          datetime,
   gmt_send             datetime comment '������������ʱ������',
   uid                  int(20),
   eid                  int(20),
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: orders                                                */
/*==============================================================*/
create table orders
(
   id                   int(20) not null auto_increment,
   order_no             varchar(50) comment '������Ψһ����ϵͳ����',
   order_no_taobao      varchar(50),
   buyer_id             int(20),
   payment              varchar(20),
   total_order_cash     float comment 'Ĭ������¶����ܶ���ڲ�Ʒ�ܽ�������ֵ�����޸ģ���Ϊ���ܻ�ȥ����ͷ��һ������ȷ�����ɺ󽫲����Ը��Ķ����ܶ�',
   total_product_cash   float comment '���ݶ�����Ʒ�嵥�����ۼ۸����ó�',
   paid                 float comment '����֧�������ڶ����ܶ�ʱ��ʾ��ȫ��֧��',
   gmt_order            datetime comment 'Ĭ����gmtcreateһ�������Ա����ģ�������ʾ���������֮���µ�ʱ��',
   order_status         tinyint default 0 comment 'һ������Ҫ��������״̬���¶������ѷ����������ۺ���׶�����������ɵĶ���',
   uid                  int(20),
   eid                  int(20),
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: orders_product                                        */
/*==============================================================*/
create table orders_product
(
   id                   int(20) not null,
   product_id           int(20),
   orders_id            int(20),
   price_discount       varchar(20) comment '�ۿۼ����ۿۻ�Ӽۺ�����ҵ����ռ۸�Ĭ�ϸ���Ʒ�۸�һ�£������ϵĲ�Ʒ�ܽ�����ۿۼۼ���ó�',
   quantity             int(11),
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: product                                               */
/*==============================================================*/
create table product
(
   id                   int(20) not null auto_increment,
   product_category_id  int(20) comment '�Բ�Ʒ���з��࣬һ����Ʒֻ������һ������',
   name                 varchar(254),
   remark               varchar(254),
   model                varchar(50),
   price_purchase       float,
   price_sale           float,
   unit                 varchar(20),
   size                 varchar(50),
   gmtcreate            datetime,
   uid                  int(20),
   eid                  int(20),
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: product_category                                      */
/*==============================================================*/
create table product_category
(
   id                   int(20) not null auto_increment,
   name                 varchar(50),
   parent_id            int(20),
   l                    int,
   r                    int,
   del                  tinyint default 0,
   gmtcreate            datetime,
   uid                  int(20),
   eid                  int(20),
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: product_image                                         */
/*==============================================================*/
create table product_image
(
   id                   int(20) not null auto_increment,
   product_id           int(20),
   filename             varchar(254) comment '�ļ����������ļ���չ���������ļ���',
   filetype             varchar(20) comment '�ļ����ͣ�����image,doc,zip��',
   filepath             varchar(254) comment '·��',
   remark               varchar(254) comment '�ļ���ǣ�һЩ��������ͼƬ���ı���Ϣ',
   gmtcreate            datetime comment '�ϴ�ʱ�䣬Ҳ�Ǽ�¼������ʱ��',
   uid                  int(20),
   eid                  int(20),
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: product_stock                                         */
/*==============================================================*/
create table product_stock
(
   id                   int(20) not null,
   product_id           int(20),
   barcode              varchar(20),
   batch                varchar(20),
   amount               float comment '���������ÿ���Ʒ�Ŀ������������������Ҳ����������',
   gmt_checkin          datetime,
   gmt_checkout         datetime comment '���ǵ�ĳЩ��Ʒ���ܻ��γ��⣬����ֻ��¼���һ�γ���ʱ��',
   amount_checkout      float comment '���ѳ��������������ʱ��ʾȫ�����⣬Ĭ�ϵ��ڿ����',
   remark               varchar(254),
   gmtcreate            datetime,
   uid                  int(20),
   eid                  int(20),
   logistics_details_id int(20),
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: sale_dispute                                          */
/*==============================================================*/
create table sale_dispute
(
   id                   int(20) not null auto_increment,
   order_id             int(20),
   return_goods         tinyint default 0,
   reparation           float,
   remark               varchar(254),
   period               datetime comment '������������ʲôʱ����Ҫ������Ҫ������ʾ���״���Ľ�����',
   severity             tinyint default 0 comment '�������۾������س̶ȣ�ϵͳ�ṩһЩ�̶���ѡ��ͣ���ͨ���ߣ�����',
   resolved             tinyint default 0 comment '������ʾ��������Ƿ��Ѵ����',
   resolved_uid         int(20),
   uid                  int(20),
   eid                  int(20),
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;