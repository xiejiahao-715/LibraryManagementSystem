create table book_borrow
(
    id               bigint               not null comment '借阅的唯一订单id'
        primary key,
    user_id          bigint               not null comment '借阅者的用户id',
    book_id          bigint               not null comment '借阅的图书id',
    borrow_book_date datetime             not null comment '借书的时间',
    return_book_date datetime             not null comment '还书的时间',
    borrow_time      int                  not null comment '借阅的时长(单位：天)',
    end_date         datetime             null comment '借阅订单完成的时间',
    status           int        default 0 not null comment '借阅订单的状态(0:借阅中,1:续借中,2:完成订单,3:超时未归还))',
    deleted          tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create       datetime             not null comment '创建时间',
    gmt_update       datetime             not null comment '修改时间'
)
    comment '图书的借阅信息表';

create table book_info
(
    id           bigint                          not null comment '图书唯一id'
        primary key,
    type_id      bigint                          null comment '图书的分类ID',
    ISBN         char(13)                        null comment '图书ISBN号',
    name         varchar(50)                     null comment '图书名称',
    author       varchar(100)                    null comment '图书作者',
    press        varchar(20)                     null comment '出版社',
    price        varchar(20)                     null comment '图书定价',
    publish_time date                            null comment '图书出版日期',
    description  text collate utf8mb4_general_ci null comment '图书简介',
    cover_img    varchar(100)                    null comment '图书封面图片的链接地址',
    deleted      tinyint(1) default 0            not null comment '逻辑删除字段',
    gmt_create   datetime                        not null comment '创建时间',
    gmt_update   datetime                        not null comment '修改时间',
    constraint book_info_ISBN_uindex
        unique (ISBN)
)
    comment '图书的基本信息表';

create table book_out_stock
(
    id             bigint               not null comment '出库记录id'
        primary key,
    book_id        bigint               not null comment '图书id',
    user_id        bigint               not null comment '负责出库管理员的id',
    out_stock_time datetime             not null comment '出库的时间',
    num            int                  not null comment '出库图书的数量',
    deleted        tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create     datetime             not null comment '创建时间',
    gmt_update     datetime             not null comment '修改时间'
)
    comment '图书出库记录表';

create table book_reserve
(
    id           bigint               not null comment '预定的唯一订单id'
        primary key,
    user_id      bigint               not null comment '预定者的用户id',
    book_id      bigint               not null comment '预定的图书id',
    reserve_time datetime             not null comment '预约的开始时间',
    notify_time  datetime             null comment '通知时间',
    status       int        default 0 not null comment '预定订单的状态(0:预定中,未通知;1:已通知,完成订单)',
    deleted      tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create   datetime             not null comment '创建时间',
    gmt_update   datetime             not null comment '修改时间'
)
    comment '图书的预定信息表';

create table book_stock
(
    book_id    bigint               not null comment '图书的唯一id'
        primary key,
    num        int        default 0 not null comment '图书的剩余数量',
    deleted    tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create datetime             not null comment '创建时间',
    gmt_update datetime             not null comment '修改时间'
)
    comment '图书的库存表';

create table book_type
(
    id         bigint               not null comment '分类唯一id'
        primary key,
    name       varchar(10)          not null comment '分类名称',
    deleted    tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create datetime             not null comment '创建时间',
    gmt_update datetime             not null comment '修改时间'
)
    comment '图书的分类表';

create table sys_permission
(
    id          bigint               not null comment '权限的唯一id'
        primary key,
    name        varchar(20)          not null comment '权限的名称',
    description tinytext             null comment '对该权限的描述',
    deleted     tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create  datetime             not null comment '创建时间',
    gmt_update  datetime             not null comment '修改时间'
)
    comment '系统的权限表';

create table sys_role
(
    id          bigint               not null comment '角色id'
        primary key,
    name        varchar(20)          not null comment '角色的名称',
    description tinytext             null comment '对角色的描述',
    deleted     tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create  datetime             not null comment '创建时间',
    gmt_update  datetime             not null comment '修改时间'
)
    comment '系统的角色表';

create table sys_role_permission_relation
(
    id            bigint not null comment '角色-权限关联的唯一id'
        primary key,
    role_id       bigint not null comment '角色id',
    permission_id bigint not null comment '权限id'
)
    comment '系统的角色-权限关联表';

create table sys_user
(
    id         bigint               not null comment '用户的唯一id'
        primary key,
    username   varchar(20)          not null comment '用户名',
    password   varchar(20)          not null comment '用户密码',
    email      varchar(25)          null comment '用户的邮箱(便于通知用户相关信息)',
    status     int        default 0 not null comment '用户账号的状态(0:正常使用;1:被禁用;2:已注销)',
    deleted    tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create datetime             not null comment '创建时间',
    gmt_update datetime             not null comment '修改时间'
)
    comment '系统的用户表';

create table sys_user_role_relation
(
    id      bigint not null comment '用户-角色关联的唯一id'
        primary key,
    user_id bigint not null comment '用户id',
    role_id bigint not null comment '角色id'
)
    comment '系统用户-角色关联表';

create table user_msg
(
    id         bigint               not null comment '唯一id'
        primary key,
    user_id    bigint               not null comment '用户id',
    send_date  datetime             null comment '系统发送消息的时间',
    message    text                 null comment '消息的内容',
    deleted    tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create datetime             not null comment '创建时间',
    gmt_update datetime             not null comment '修改时间'
)
    comment '用户消息表';

create table user_resource
(
    user_id        bigint               not null comment '用户id'
        primary key,
    borrow_max_num int                  null comment '每个用户能借阅的最多图书数量',
    borrowed_num   int                  null comment '用户已经借阅图书的数量',
    deleted        tinyint(1) default 0 not null comment '逻辑删除字段',
    gmt_create     datetime             not null comment '创建时间',
    gmt_update     datetime             not null comment '修改时间'
)
    comment '借阅者(普通用户)的资源信息';

