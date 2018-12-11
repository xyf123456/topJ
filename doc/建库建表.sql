DROP DATABASE topJ_db;

CREATE DATABASE topJ_db;

-- 人员信息
-- DROP TABLE sys_user;
CREATE TABLE sys_user
(
  userid     INT AUTO_INCREMENT PRIMARY KEY     COMMENT '人员编号',
  username   VARCHAR(10) NOT NULL COMMENT '帐号',
  `password` VARCHAR(10) NOT NULL COMMENT '密码',
  roleid     INT NOT NULL COMMENT '角色编号'
) ENGINE=INNODB COMMENT='人员信息';

INSERT INTO sys_user VALUES
(NULL, 'admin', '123', 1), (NULL, 'test', '456', 2);

-- 角色信息
-- DROP TABLE sys_role;
CREATE TABLE sys_role
(
  roleid   INT AUTO_INCREMENT PRIMARY KEY COMMENT '角色编号',
  rolename VARCHAR(10) NOT NULL COMMENT '角色名称'
) ENGINE=INNODB COMMENT='角色信息';

INSERT INTO sys_role VALUES(NULL, '管理员'), (NULL, '一般用户');

-- 资源信息
-- DROP TABLE sys_resource;
CREATE TABLE sys_resource
(
  resourceid   INT AUTO_INCREMENT PRIMARY KEY COMMENT '资源编号',
  resourcepid  INT          NOT NULL COMMENT '资源父编号',
  resourcename VARCHAR(50)  NOT NULL COMMENT '资源名称',
  resourceicon VARCHAR(50)  NOT NULL COMMENT '资源图标',
  resourceurl  VARCHAR(500) NOT NULL COMMENT '资源URL'
) ENGINE=INNODB COMMENT='资源信息';

INSERT INTO sys_resource VALUES
(NULL, 0, '权限管理', 'icon-sys', '/system'),
(NULL, 1, '用户管理', 'icon-nav', '/user/view'),
(NULL, 1, '角色管理', 'icon-nav', '/role/view'),
(NULL, 1, '资源管理', 'icon-nav', '/resource/view'),

(NULL, 2, '加载全部', 'icon-refresh', '/user/loadall'),
(NULL, 2, '搜索', 'icon-search', '/user/search'),
(NULL, 2, '新增', 'icon-add', '/user/save'),
(NULL, 2, '编辑', 'icon-edit', '/user/update'),
(NULL, 2, '删除', 'icon-delete', '/user/delete'),

(NULL, 3, '加载全部', 'icon-refresh', '/role/loadall'),
(NULL, 3, '搜索', 'icon-search', '/role/search'),
(NULL, 3, '新增', 'icon-add', '/role/save'),
(NULL, 3, '编辑', 'icon-edit', '/role/update'),
(NULL, 3, '删除', 'icon-delete', '/role/delete'),
(NULL, 3, '分配', 'icon-config', '/role/config'),

(NULL, 0, '业务管理', 'icon-sys', '/biz'),
(NULL, 16, '邮件管理', 'icon-nav', '/mail/view');

-- 角色资源关联
-- DROP TABLE sys_role_resource;
CREATE TABLE sys_role_resource
(
  roleresourceid INT AUTO_INCREMENT PRIMARY KEY COMMENT '编号',
  roleid     INT NOT NULL COMMENT '角色编号',
  resourceid INT NOT NULL COMMENT '资源编号'
) ENGINE=INNODB COMMENT='角色资源关联';

INSERT INTO sys_role_resource VALUES
(NULL, 1, 1), (NULL, 1, 2), (NULL, 1, 3), (NULL, 1, 4), (NULL, 1, 5), (NULL, 1, 6), (NULL, 1, 7), (NULL, 1, 8), (NULL, 1, 9),
(NULL, 1, 10), (NULL, 1, 11), (NULL, 1, 12), (NULL, 1, 13), (NULL, 1, 14), (NULL, 1, 15), (NULL, 1, 16), (NULL, 1, 17),
(NULL, 2, 1), (NULL, 2, 16), (NULL, 2, 17);