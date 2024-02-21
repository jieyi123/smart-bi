-- 创建库
create database if not exists smart_bi;

-- 切换库
use smart_bi;

-- 用户表
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

INSERT INTO `user`
VALUES (1, 'pjieyi', 'pjieyi', '13222222222', '132',
        'https://pjieyi.oss-cn-chengdu.aliyuncs.com/big-event-heima/8f3036ad-aa46-4ffa-bf2c-7de3b6129451.jpg', 1,
        'admin', '0b6f76a5e18b654393f186d4ed28a072', '2024-01-24 17:45:46', '2024-02-02 10:34:46', 0);
INSERT INTO `user`
VALUES (2, 'zs1234', 'zhangsan', '13212312313', '12345@qq.com',
        'https://pjieyi.oss-cn-chengdu.aliyuncs.com/public/ffd16276-422b-43f0-b1d1-a83473322ce4.jpg', 0, 'admin',
        '56191e5f69f082ef3e10280f2ad31672', '2024-01-25 20:57:19', '2024-02-07 20:39:37', 0);
INSERT INTO `user`
VALUES (3, 'zsss', '张三三三', '13222222221', '123@11.com', NULL, 1, 'user', '0b6f76a5e18b654393f186d4ed28a072',
        '2024-01-28 16:13:07', '2024-02-01 12:05:32', 0);

-- 图表信息表
create table if not exists chart
(
    id         bigint auto_increment comment 'id' primary key,
    name       varchar(128)                       null comment '图表名称',
    goal       text                               null comment '分析目标',
    chartData  text                               null comment '图表数据',
    chartType  varchar(128)                       null comment '图表类型',
    genChart   text                               null comment '生成的图表数据',
    genResult  text                               null comment '生成的分析结论',
    userId     bigint                             null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表';