-- 用户表
create table `customer-center`.user
(
    id           int                                      not null    primary key,
    nickname     varchar(50)  default ''                  not null,
    user_account varchar(50)  default ''                  not null,
    password     varchar(50)  default ''                  not null,
    tags         varchar(1024)                            null comment '标签 json 列表',
    is_valid     tinyint      default 1                   not null,
    create_time  datetime     default current_timestamp() not null,
    update_time  datetime     default current_timestamp() not null on update current_timestamp(),
    is_deleted   tinyint      default 0                   not null,
    description  varchar(256) default ''                  not null comment '用户描述',
    user_role    tinyint      default 0                   not null comment '用户权限 0-管理员 1-普通用户'
);

-- 队伍表
create table team
(
    id     bigint auto_increment comment 'id'   primary key,
    name   varchar(256)                   not null comment '队伍名称',
    description varchar(1024)                      null comment '描述',
    maxNum    int      default 1                 not null comment '最大人数',
    expireTime    datetime  null comment '过期时间',
    userId            bigint comment '用户id',
    status    int      default 0                 not null comment '0 - 公开，1 - 私有，2 - 加密',
    password varchar(512)                       null comment '密码',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null comment '是否删除'
)
comment '队伍';

-- 用户队伍关系
create table user_team
(
    id           bigint auto_increment comment 'id'     primary key,
    userId       bigint comment '用户id',
    teamId       bigint comment '队伍id',
    joinTime     datetime                           null comment '加入时间',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null comment '是否删除'
)
comment '用户队伍关系';