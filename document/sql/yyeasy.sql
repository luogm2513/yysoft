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
   account              varchar(50) comment '对应权限系统的账户（非uid）',
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
   valuable             tinyint default 0 comment '卖家对买家的一个主观评价',
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
   costs                float comment '发货时的物流费用，最后会计到成本里面',
   gmt_receive          datetime,
   gmt_send             datetime comment '用来标记网店何时发货的',
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
   order_no             varchar(50) comment '订单号唯一，由系统生成',
   order_no_taobao      varchar(50),
   buyer_id             int(20),
   payment              varchar(20),
   total_order_cash     float comment '默认情况下订单总额等于产品总金额，但该数值可以修改，因为可能会去掉零头，一量订单确认生成后将不可以更改订单总额',
   total_product_cash   float comment '根据订单产品清单的销售价格计算得出',
   paid                 float comment '当已支付金额等于订单总额时表示已全部支付',
   gmt_order            datetime comment '默认与gmtcreate一样，可以被更改，用来表示买家与卖家之间下单时间',
   order_status         tinyint default 0 comment '一个订单要经历以下状态：新订单，已发货订单，售后纠纷订单，交易完成的订单',
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
   price_discount       varchar(20) comment '折扣价是折扣或加价后给卖家的最终价格，默认跟产品价格一致，订单上的产品总金额由折扣价计算得出',
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
   product_category_id  int(20) comment '对产品进行分类，一个产品只能属于一个分类',
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
   filename             varchar(254) comment '文件名，包括文件扩展名的完整文件名',
   filetype             varchar(20) comment '文件类型，例：image,doc,zip等',
   filepath             varchar(254) comment '路径',
   remark               varchar(254) comment '文件标记，一些用于描述图片的文本信息',
   gmtcreate            datetime comment '上传时间，也是记录创建的时间',
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
   amount               float comment '用于描述该库存产品的库存量，可能是数量，也可能是重量',
   gmt_checkin          datetime,
   gmt_checkout         datetime comment '考虑到某些产品可能会多次出库，这里只记录最后一次出库时间',
   amount_checkout      float comment '当已出库量等于入库量时表示全部出库，默认等于库存量',
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
   period               datetime comment '用来描述最晚什么时候需要处理，主要用来表示纠纷处理的紧迫性',
   severity             tinyint default 0 comment '主观评价纠纷严重程度，系统提供一些固定的选项，低，普通，高，紧迫',
   resolved             tinyint default 0 comment '用来表示这个纠纷是否已处理掉',
   resolved_uid         int(20),
   uid                  int(20),
   eid                  int(20),
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;