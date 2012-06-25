/*==============================================================*/
/* Table: auth_dept                                             */
/*==============================================================*/
create table auth_dept
(
   id                   int(20) not null auto_increment,
   parent_id            int(20) default 0 comment '�ϼ�����',
   name                 varchar(50) comment '��������',
   remark               varchar(254),
   blocked              tinyint default 0,
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: auth_dept_right                                       */
/*==============================================================*/
create table auth_dept_right
(
   dept_id              bigint(20),
   right_id             bigint(20)
)
type = MYISAM;

/*==============================================================*/
/* Table: auth_right                                            */
/*==============================================================*/
create table auth_right
(
   id                   int(20) not null auto_increment,
   parent_id            int(20) comment '��Ȩ��',
   name                 varchar(50) comment 'Ȩ����',
   content              varchar(254) comment 'Ȩ��',
   l                    int,
   r                    int,
   menu                 varchar(50) comment '�˵���',
   menu_url             varchar(254) comment '�˵�URL',
   menu_css             varchar(50),
   gmt_created          datetime comment '����ʱ��',
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: auth_role                                             */
/*==============================================================*/
create table auth_role
(
   id                   int(20) not null auto_increment,
   name                 varchar(50) comment '��ɫ����',
   remark               varchar(254) comment '��ɫkey',
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: auth_role_right                                       */
/*==============================================================*/
create table auth_role_right
(
   right_id             int(20),
   role_id              int(20)
)
type = MYISAM;

/*==============================================================*/
/* Table: auth_user                                             */
/*==============================================================*/
create table auth_user
(
   id                   int(20) not null auto_increment,
   dept_id              int(20) comment '����',
   username             varchar(50) not null comment '�û���',
   password             varchar(32) not null comment '����',
   email                varchar(50),
   gmt_created          datetime comment '����ʱ��',
   gmt_login            datetime comment '����¼ʱ��',
   blocked              tinyint default 0,
   primary key (id)
)
type = MYISAM;

/*==============================================================*/
/* Table: auth_user_role                                        */
/*==============================================================*/
create table auth_user_role
(
   role_id              int(20),
   user_id              int(20)
)
type = MYISAM;


/*==============================================================*/
/* Table: param_type                                            */
/*==============================================================*/
create table param_type
(
   type                 varchar(64) not null,
   names                varchar(254),
   gmtcreate            datetime,
   primary key (type)
)
type = MYISAM; 


/*==============================================================*/
/* Table: param                                                 */
/*==============================================================*/
create table param
(
   id                   int(20) not null auto_increment,
   type                 varchar(64),
   names                varchar(254),
   `key`                  varchar(254),
   value                varchar(254),
   sort                 tinyint,
   used                 tinyint default 0,
   gmtcreate            datetime,
   primary key (id)
)
type = MYISAM;