-- 用户基础信息
create table User
(
    user_id      bigint        not null comment '用户ID' primary key,
    account      nvarchar(256) not null comment '账号' unique,
    password     nvarchar(256) not null default '123456' comment '密码',
    name         nvarchar(256) null comment '用户昵称',
    avatar       nvarchar(256) null comment '头像',
    phone        nvarchar(256) null comment '手机号码',
    email        nvarchar(256) null comment '邮箱',
    sex          tinyint       null comment '性别',
    birthday     datetime      null comment '出生日期',
    birthplace   nvarchar(255) null comment '籍贯',
    status       tinyint       not null default 1 comment '用户状态 0被禁用 1正常',
    role         tinyint       not null default 1 comment '用户角色 1普通用户 2管理员',
    gmt_created  TIMESTAMP              DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) comment '用户表' collate = utf8mb4_unicode_ci;


-- 登录日志表
create table Login_Log
(
    id             bigint comment 'id' primary key,
    user_id        bigint       not null comment '用户ID',
    login_ip       nvarchar(32) not null comment '登录IP',
    login_time     bigint       not null comment '登录时间',
    login_platform tinyint      not null comment '登录平台 1微信小程序 2PC端 3安卓APP 4苹果APP 5iOS平板',
    gmt_created    TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
) comment '用户登录日志表' collate = utf8mb4_unicode_ci;

-- 手机号短信留痕表
create table PhoneMessage_Log
(
    id            int auto_increment not null comment 'id' primary key,
    phone         nvarchar(64)       not null comment '手机号',
    outer_message nvarchar(512)      null comment '外部响应内容',
    gmt_created   TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
) comment '手机号短信记录表' collate = utf8mb4_unicode_ci;

-- 第三方登录表
create table UserThirdAuthorization
(
    id               int auto_increment not null primary key,
    personnel_id     bigint             not null comment '人员ID',
    app_platform     int                not null comment 'app平台类型',
    third_type       int                not null comment '第三方授权类型',
    third_first_tag  nvarchar(128)      not null comment '第三方平台一级标志',
    third_second_tag nvarchar(128)      null comment '第三方平台二级标志 考虑到微信OPENID存在不同的情况',
    gmt_created      TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) comment '用户第三方登录授权表' collate = utf8mb4_unicode_ci;
ALTER TABLE UserThirdAuthorization
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (personnel_id) REFERENCES User (user_id) on delete cascade;

-- 昵称生成表
create table NickNameRepo
(
    id      int auto_increment primary key not null comment 'id',
    type    int                            not null comment '单词类型',
    content nvarchar(128)                  not null comment '单词内容'
) comment '昵称工具表' collate = utf8mb4_unicode_ci;

create table Local_Media
(
    id          bigint primary key not null comment 'id',
    url         nvarchar(512)      not null comment '多媒体URL',
    url_tag_id  bigint             null comment '多媒体标签',
    gmt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
) comment '本地多媒体文件表' collate = utf8mb4_unicode_ci;

create table Local_Media_Tag
(
    id          bigint primary key not null comment 'id',
    tag_name    nvarchar(255)      not null comment '本地多媒体文件标签名',
    gmt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
) comment '本地多媒体文件标签表' collate = utf8mb4_unicode_ci;

create table System_Log
(
    id          bigint primary key not null comment 'id',
    interface   nvarchar(512)      not null comment '接口名',
    token       nvarchar(256)      null comment 'token',
    gmt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
)comment '系统调用日志' collate = utf8mb4_unicode_ci;