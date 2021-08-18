drop database if exists miaosha;

create database miaosha default charset = utf8 collate = utf8_unicode_ci;

use miaosha;

create table user_info
(
    id             int         not null auto_increment comment '用户主键id',
    name           varchar(64) not null default '' comment '用户名称',
    gender         tinyint     not null default 0 comment '0:女性, 1:男性',
    age            int         not null default 0 comment '用户年龄',
    telphone       varchar(11) not null default '' comment '用户手机号',
    register_mode  varchar(10) not null default 'byphone' comment 'byphone, bywechat, byalipay',
    third_party_id varchar(64) not null default '' comment '第三方账户名',
    primary key (id),
    unique index telphone_unique_index (telphone)
);

create table user_password
(
    id               int          not null auto_increment comment '自增主键',
    encrypt_password varchar(128) not null default '' comment '加密用户密码, 对用户负责的态度',
    user_id          int          not null default 0 comment '对应user_info中的某个用户',
    primary key (id),
    unique index user_id_idx (user_id)
);

create table item
(
    id          int          not null auto_increment comment '自增主键',
    title       varchar(64)  not null default '' comment '商品名称',
    description varchar(512) not null default '' comment '商品描述',
    sales       int          not null default 0 comment '销量',
    price       double       not null default 0 comment '商品价格',
    image_url   varchar(500) not null default '' comment '图片链接',
    primary key (id)
);

create table item_stock
(
    id      int not null auto_increment comment '自增主键',
    stock   int not null default 0 comment '商品库存',
    item_id int not null default 0 comment '商品id',
    primary key (id),
    index item_id_idx (item_id)
);