/*
MySQL Data Transfer
Source Host: sopun.gotoftp5.com
Source Database: sopun
Target Host: sopun.gotoftp5.com
Target Database: sopun
Date: 2010/5/23 21:00:26
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for auth_dept
-- ----------------------------
CREATE TABLE `auth_dept` (
  `id` int(20) NOT NULL auto_increment,
  `parent_id` int(20) default '0' COMMENT '上级部门',
  `name` varchar(50) default NULL COMMENT '部门名称',
  `remark` varchar(254) default NULL,
  `blocked` tinyint(4) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_dept_right
-- ----------------------------
CREATE TABLE `auth_dept_right` (
  `dept_id` bigint(20) default NULL,
  `right_id` bigint(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_right
-- ----------------------------
CREATE TABLE `auth_right` (
  `id` int(20) NOT NULL auto_increment,
  `parent_id` int(20) default NULL COMMENT '父权限',
  `name` varchar(50) default NULL COMMENT '权限名',
  `content` varchar(254) default NULL COMMENT '权限',
  `l` int(11) default NULL,
  `r` int(11) default NULL,
  `menu` varchar(50) default NULL COMMENT '菜单名',
  `menu_url` varchar(254) default NULL COMMENT '菜单URL',
  `menu_css` varchar(50) default NULL,
  `gmt_created` datetime default NULL COMMENT '创建时间',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
CREATE TABLE `auth_role` (
  `id` int(20) NOT NULL auto_increment,
  `name` varchar(50) default NULL COMMENT '角色名字',
  `remark` varchar(254) default NULL COMMENT '角色key',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_role_right
-- ----------------------------
CREATE TABLE `auth_role_right` (
  `right_id` int(20) default NULL,
  `role_id` int(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
CREATE TABLE `auth_user` (
  `id` int(20) NOT NULL auto_increment,
  `dept_id` int(20) default NULL COMMENT '部门',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `email` varchar(50) default NULL,
  `gmt_created` datetime default NULL COMMENT '创建时间',
  `gmt_login` datetime default NULL COMMENT '最后登录时间',
  `blocked` tinyint(4) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for auth_user_role
-- ----------------------------
CREATE TABLE `auth_user_role` (
  `role_id` int(20) default NULL,
  `user_id` int(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for eshop
-- ----------------------------
CREATE TABLE `eshop` (
  `id` int(20) NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `site_url` varchar(254) default NULL,
  `remark` varchar(254) default NULL,
  `gmtcreate` datetime default NULL,
  `domain` varchar(254) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for eshop_account
-- ----------------------------
CREATE TABLE `eshop_account` (
  `id` int(20) NOT NULL auto_increment,
  `eid` int(20) default NULL,
  `uid` int(20) default NULL,
  `account` varchar(50) default NULL COMMENT '对应权限系统的账户（非uid）',
  `name` varchar(20) default NULL,
  `blocked` tinyint(4) default NULL,
  `gmtcreate` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for eshop_buyer
-- ----------------------------
CREATE TABLE `eshop_buyer` (
  `id` int(20) NOT NULL auto_increment,
  `name` varchar(20) default NULL,
  `phone` varchar(20) default NULL,
  `email` varchar(50) default NULL,
  `im` varchar(50) default NULL,
  `valuable` tinyint(4) default '0' COMMENT '卖家对买家的一个主观评价',
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  `gmtcreate` datetime default NULL,
  `remark` varchar(254) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for logistics
-- ----------------------------
CREATE TABLE `logistics` (
  `id` int(20) NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `phone` varchar(20) default NULL,
  `address` varchar(254) default NULL,
  `routes` varchar(254) default NULL,
  `arrival_area` varchar(50) default NULL,
  `remark` varchar(254) default NULL,
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  `gmtcreate` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for logistics_details
-- ----------------------------
CREATE TABLE `logistics_details` (
  `id` int(20) NOT NULL auto_increment,
  `order_id` int(20) default NULL,
  `logistics_id` int(20) default NULL,
  `express_no` varchar(50) default NULL,
  `costs` float default NULL COMMENT '发货时的物流费用，最后会计到成本里面',
  `gmt_receive` datetime default NULL,
  `gmt_send` datetime default NULL COMMENT '用来标记网店何时发货的',
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  `gmtcreate` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
CREATE TABLE `orders` (
  `id` int(20) NOT NULL auto_increment,
  `order_no` varchar(50) default NULL COMMENT '订单号唯一，由系统生成',
  `order_no_taobao` varchar(50) default NULL,
  `buyer_id` int(20) default NULL,
  `payment` varchar(20) default NULL,
  `total_order_cash` float default NULL COMMENT '默认情况下订单总额等于产品总金额，但该数值可以修改，因为可能会去掉零头，一量订单确认生成后将不可以更改订单总额',
  `total_product_cash` float default NULL COMMENT '根据订单产品清单的销售价格计算得出',
  `paid` float default NULL COMMENT '当已支付金额等于订单总额时表示已全部支付',
  `gmt_order` datetime default NULL COMMENT '默认与gmtcreate一样，可以被更改，用来表示买家与卖家之间下单时间',
  `order_status` tinyint(4) default '0' COMMENT '一个订单要经历以下状态：新订单，已发货订单，售后纠纷订单，交易完成的订单',
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  `gmtcreate` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orders_product
-- ----------------------------
CREATE TABLE `orders_product` (
  `id` int(20) NOT NULL auto_increment,
  `product_id` int(20) default NULL,
  `orders_id` int(20) default NULL,
  `price_discount` varchar(20) default NULL COMMENT '折扣价是折扣或加价后给卖家的最终价格，默认跟产品价格一致，订单上的产品总金额由折扣价计算得出',
  `quantity` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for param
-- ----------------------------
CREATE TABLE `param` (
  `id` int(20) NOT NULL auto_increment,
  `type` varchar(64) default NULL,
  `names` varchar(254) default NULL,
  `key` varchar(254) default NULL,
  `value` varchar(254) default NULL,
  `sort` tinyint(4) default NULL,
  `used` tinyint(4) default '0',
  `gmtcreate` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for param_type
-- ----------------------------
CREATE TABLE `param_type` (
  `type` varchar(64) NOT NULL,
  `names` varchar(254) default NULL,
  `gmtcreate` datetime default NULL,
  PRIMARY KEY  (`type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product
-- ----------------------------
CREATE TABLE `product` (
  `id` int(20) NOT NULL auto_increment,
  `product_category_id` int(20) default NULL COMMENT '对产品进行分类，一个产品只能属于一个分类',
  `name` varchar(254) default NULL,
  `remark` varchar(254) default NULL,
  `model` varchar(50) default NULL,
  `price_purchase` float default NULL,
  `price_sale` float default NULL,
  `unit` varchar(20) default NULL,
  `size` varchar(50) default NULL,
  `gmtcreate` datetime default NULL,
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
CREATE TABLE `product_category` (
  `id` int(20) NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `parent_id` int(20) default NULL,
  `l` int(11) default NULL,
  `r` int(11) default NULL,
  `del` tinyint(4) default '0',
  `gmtcreate` datetime default NULL,
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product_image
-- ----------------------------
CREATE TABLE `product_image` (
  `id` int(20) NOT NULL auto_increment,
  `product_id` int(20) default NULL,
  `filename` varchar(254) default NULL COMMENT '文件名，包括文件扩展名的完整文件名',
  `filetype` varchar(20) default NULL COMMENT '文件类型，例：image,doc,zip等',
  `filepath` varchar(254) default NULL COMMENT '路径',
  `remark` varchar(254) default NULL COMMENT '文件标记，一些用于描述图片的文本信息',
  `gmtcreate` datetime default NULL COMMENT '上传时间，也是记录创建的时间',
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product_stock
-- ----------------------------
CREATE TABLE `product_stock` (
  `id` int(20) NOT NULL auto_increment,
  `product_id` int(20) default NULL,
  `barcode` varchar(20) default NULL,
  `batch` varchar(20) default NULL,
  `amount` float default NULL COMMENT '用于描述该库存产品的库存量，可能是数量，也可能是重量',
  `gmt_checkin` datetime default NULL,
  `gmt_checkout` datetime default NULL COMMENT '考虑到某些产品可能会多次出库，这里只记录最后一次出库时间',
  `amount_checkout` float default NULL COMMENT '当已出库量等于入库量时表示全部出库，默认等于库存量',
  `remark` varchar(254) default NULL,
  `gmtcreate` datetime default NULL,
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  `logistics_details_id` int(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sale_dispute
-- ----------------------------
CREATE TABLE `sale_dispute` (
  `id` int(20) NOT NULL auto_increment,
  `order_id` int(20) default NULL,
  `return_goods` tinyint(4) default '0',
  `reparation` float default NULL,
  `remark` varchar(254) default NULL,
  `period` datetime default NULL COMMENT '用来描述最晚什么时候需要处理，主要用来表示纠纷处理的紧迫性',
  `severity` tinyint(4) default '0' COMMENT '主观评价纠纷严重程度，系统提供一些固定的选项，低，普通，高，紧迫',
  `resolved` tinyint(4) default '0' COMMENT '用来表示这个纠纷是否已处理掉',
  `resolved_uid` int(20) default NULL,
  `uid` int(20) default NULL,
  `eid` int(20) default NULL,
  `gmtcreate` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
