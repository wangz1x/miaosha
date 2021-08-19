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

insert into item (title, description, sales, price, image_url)
values ('iphone11', '很好看的苹果手机', 0, 4999,
        'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/iphone11-select-2019-family?wid=882&hei=1058&fmt=jpeg&qlt=80&.v=1567022175704'),
       ('iphone12', '不是圆角的苹果手机', 0, 5999,
        'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/iphone-12-pro-family-hero?wid=940&hei=1112&fmt=jpeg&qlt=80&.v=1604021663000'),
       ('iphone13', '还没发售的苹果手机', 0, 9999,
        'https://thumbor.forbes.com/thumbor/711x401/https://specials-images.forbesimg.com/imageserve/61055552b6d96ce71cdff7be/Apple-iPhone-13--iPhone-13-Pro--iPhone-13-Pro-Max--iPhone-13-Mini--new-iPhone/960x0.jpg?fit=scale');

create table item_stock
(
    id      int not null auto_increment comment '自增主键',
    stock   int not null default 0 comment '商品库存',
    item_id int not null default 0 comment '商品id',
    primary key (id),
    index item_id_idx (item_id)
);

insert into item_stock (stock, item_id)
values (100, 1),
       (10, 2),
       (5, 3);

create table order_info
(
    id          varchar(32) not null default '' comment '主键',
    user_id     int         not null default 0 comment '下单用户id',
    item_id     int         not null default 0 comment '下单商品id',
    item_price  double      not null default 0.0 comment '下单时商品单价',
    amount      int         not null default 0 comment '订单商品个数',
    order_price double      not null default 0.0 comment '订单总价',
    primary key (id)
);

create table sequence_info
(
    name          varchar(40) not null comment '自增主键',
    current_value int         not null default 0 comment '当前序列值',
    step          int         not null default 0 comment '自增步长',
    primary key (name)
);

insert into sequence_info
values ('order_info', 0, 1);

create table promo
(
    id          int          not null auto_increment comment '自增主键',
    promo_name  varchar(120) not null default '' comment '营销名称',
    start_time  datetime     not null comment '活动起始时间',
    end_time    datetime     not null comment '活动结束时间',
    item_id     int          not null default 0 comment '活动适用产品id',
    promo_price double       not null default 0.0 comment '活动售价',
    primary key (id),
    index item_id_idx (item_id)
) auto_increment = 1000;

insert into promo (promo_name, start_time, end_time, item_id, promo_price)
values ('999.99秒杀iphone11', '2021-08-19 00:00:00', '2021-08-20 00:00:00', 1, 999.99);