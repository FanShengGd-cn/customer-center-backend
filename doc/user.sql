create table `customer-center`.user
(
    id           int                                      not null
        primary key,
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