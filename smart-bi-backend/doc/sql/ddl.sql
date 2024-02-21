-- 创建库
create database if not exists yi_api;

-- 切换库
use yi_api;


create table if not exists user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    phone        varchar(255)                           null comment '手机号',
    email        varchar(255)                           null comment '邮箱',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别 0女 1男',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           null comment '密码',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_phone
        unique (phone),
    constraint uni_userAccount
        unique (userAccount)
)
    comment '用户';