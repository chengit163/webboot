/*
Navicat MySQL Data Transfer

*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID(主键)',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `action` varchar(100) DEFAULT NULL COMMENT '动作',
  `url` varchar(50) DEFAULT NULL COMMENT '请求URL',
  `method` tinyint(4) DEFAULT '0' COMMENT '方法@@@{"-1":"UNKNOWN","0":"GET","1":"POST","2":"PUT","3":"DELETE","4":"HEADER","5":"OPTIONS"}',
  `args` mediumtext COMMENT '参数',
  `func` varchar(200) DEFAULT NULL COMMENT '函数',
  `os` varchar(50) DEFAULT NULL COMMENT '系统',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `cost` int(10) DEFAULT NULL COMMENT '耗时',
  `happen` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '菜单ID(主键)',
  `pid` int(10) NOT NULL DEFAULT '0' COMMENT '父菜单ID',
  `name` varchar(50) NOT NULL COMMENT '名称###{"min":"1","max":"50"}',
  `url` varchar(100) DEFAULT NULL COMMENT '路径###{"min":"0","max":"100"}',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限###{"min":"0","max":"100"}',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标###{"min":"0","max":"100"}',
  `type` tinyint(4) DEFAULT '0' COMMENT '类型@@@{"-1":"其他","0":"目录","1":"菜单","2":"按钮"}',
  `orders` int(10) DEFAULT NULL COMMENT '排序',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COMMENT='菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', null, null, 'fa fa-leaf red', '0', null, null, null);
INSERT INTO `sys_menu` VALUES ('2', '1', '菜单管理', '/system/menu/main', 'system:menu:get', 'fa fa-bars red', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('3', '2', '新增', null, 'system:menu:save', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('4', '2', '编辑', null, 'system:menu:update', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('5', '2', '删除', null, 'system:menu:remove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('6', '1', '角色管理', '/system/role/main', 'system:role:get', 'fa fa-user-circle-o red', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('7', '6', '新增', null, 'system:role:save', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('8', '6', '编辑', null, 'system:role:update', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('9', '6', '删除', null, 'system:role:remove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('10', '6', '批量删除', null, 'system:role:batchRemove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('11', '6', '导出', null, 'system:role:export', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('12', '6', '导入', null, 'system:role:import', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('13', '1', '用户管理', '/system/user/main', 'system:user:get', 'fa fa-user red', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('14', '13', '新增', null, 'system:user:save', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('15', '13', '编辑', null, 'system:user:update', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('16', '13', '删除', null, 'system:user:remove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('17', '13', '批量删除', null, 'system:user:batchRemove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('18', '13', '导出', null, 'system:user:export', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('19', '13', '导入', null, 'system:user:import', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('20', '0', '系统工具', null, null, 'fa fa-wrench blue', '0', null, null, null);
INSERT INTO `sys_menu` VALUES ('21', '20', '代码生成器', '/common/generator/main', 'common:generator:get', 'fa fa-code blue', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('22', '21', '生成代码', null, 'common:generator:code', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('23', '21', '批量生成代码', null, 'common:generator:batchCode', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('24', '21', '导出', null, 'common:generator:export', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('25', '20', 'Swagger', '/swagger-ui.html', null, 'fa fa-info blue', '-1', null, null, null);
INSERT INTO `sys_menu` VALUES ('26', '0', '系统监控', null, null, 'fa fa-desktop green', '0', null, null, null);
INSERT INTO `sys_menu` VALUES ('27', '26', '日志监控', '/common/log/main', 'common:log:get', 'fa fa-umbrella green', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('28', '27', '删除', null, 'common:log:remove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('29', '27', '批量删除', null, 'common:log:batchRemove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('30', '27', '导出', null, 'common:log:export', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('31', '26', 'Druid Monitor', '/druid/index.html', null, 'fa fa-database green', '-1', null, null, null);
INSERT INTO `sys_menu` VALUES ('32', '6', '查看菜单', null, 'rabc:role:menu:get', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('33', '6', '修改菜单', null, 'rabc:role:menu:save', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('34', '13', '查看角色', null, 'rabc:user:role:get', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('35', '13', '修改角色', null, 'rabc:user:role:save', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('36', '13', '修改密码', null, 'system:user:password', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('37', '26', '在线管理', '/system/online/main', 'system:online:get', 'fa fa-universal-access green', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('38', '37', '强制下线', null, 'system:online:remove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('39', '20', '任务计划', '/common/job/main', 'common:job:get', 'fa fa-tasks blue', '1', null, null, null);
INSERT INTO `sys_menu` VALUES ('40', '39', '新增', null, 'common:job:save', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('41', '39', '编辑', null, 'common:job:update', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('42', '39', '删除', null, 'common:job:remove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('43', '39', '批量删除', null, 'common:job:batchRemove', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('44', '39', '导出', null, 'common:job:export', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('45', '39', '导入', null, 'common:job:import', null, '2', null, null, null);
INSERT INTO `sys_menu` VALUES ('46', '39', '执行', null, 'common:job:run', null, '2', null, null, null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '角色ID(主键)',
  `name` varchar(50) NOT NULL COMMENT '名称###{"min":"1","max":"50"}',
  `description` varchar(250) DEFAULT NULL COMMENT '描述###{"min":"0","max":"250"}',
  `orders` int(10) DEFAULT NULL COMMENT '排序',
  `user_id_create` int(10) DEFAULT NULL COMMENT '创建用户ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', null, null, null, null, null);
INSERT INTO `sys_role` VALUES ('2', '普通用户', '', null, null, null, null);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` int(10) NOT NULL COMMENT '角色ID',
  `menu_id` int(10) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_id` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色_菜单_关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('2', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('3', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('4', '1', '4');
INSERT INTO `sys_role_menu` VALUES ('5', '1', '5');
INSERT INTO `sys_role_menu` VALUES ('6', '1', '6');
INSERT INTO `sys_role_menu` VALUES ('7', '1', '7');
INSERT INTO `sys_role_menu` VALUES ('8', '1', '8');
INSERT INTO `sys_role_menu` VALUES ('9', '1', '9');
INSERT INTO `sys_role_menu` VALUES ('10', '1', '10');
INSERT INTO `sys_role_menu` VALUES ('11', '1', '11');
INSERT INTO `sys_role_menu` VALUES ('12', '1', '12');
INSERT INTO `sys_role_menu` VALUES ('13', '1', '13');
INSERT INTO `sys_role_menu` VALUES ('14', '1', '14');
INSERT INTO `sys_role_menu` VALUES ('15', '1', '15');
INSERT INTO `sys_role_menu` VALUES ('16', '1', '16');
INSERT INTO `sys_role_menu` VALUES ('17', '1', '17');
INSERT INTO `sys_role_menu` VALUES ('18', '1', '18');
INSERT INTO `sys_role_menu` VALUES ('19', '1', '19');
INSERT INTO `sys_role_menu` VALUES ('20', '1', '20');
INSERT INTO `sys_role_menu` VALUES ('21', '1', '21');
INSERT INTO `sys_role_menu` VALUES ('22', '1', '22');
INSERT INTO `sys_role_menu` VALUES ('23', '1', '23');
INSERT INTO `sys_role_menu` VALUES ('24', '1', '24');
INSERT INTO `sys_role_menu` VALUES ('25', '1', '25');
INSERT INTO `sys_role_menu` VALUES ('26', '1', '26');
INSERT INTO `sys_role_menu` VALUES ('27', '1', '27');
INSERT INTO `sys_role_menu` VALUES ('28', '1', '28');
INSERT INTO `sys_role_menu` VALUES ('29', '1', '29');
INSERT INTO `sys_role_menu` VALUES ('30', '1', '30');
INSERT INTO `sys_role_menu` VALUES ('31', '1', '31');
INSERT INTO `sys_role_menu` VALUES ('32', '1', '32');
INSERT INTO `sys_role_menu` VALUES ('33', '1', '33');
INSERT INTO `sys_role_menu` VALUES ('34', '1', '34');
INSERT INTO `sys_role_menu` VALUES ('35', '1', '35');
INSERT INTO `sys_role_menu` VALUES ('36', '1', '36');
INSERT INTO `sys_role_menu` VALUES ('37', '1', '37');
INSERT INTO `sys_role_menu` VALUES ('38', '1', '38');
INSERT INTO `sys_role_menu` VALUES ('39', '1', '39');
INSERT INTO `sys_role_menu` VALUES ('40', '1', '40');
INSERT INTO `sys_role_menu` VALUES ('41', '1', '41');
INSERT INTO `sys_role_menu` VALUES ('42', '1', '42');
INSERT INTO `sys_role_menu` VALUES ('43', '1', '43');
INSERT INTO `sys_role_menu` VALUES ('44', '1', '44');
INSERT INTO `sys_role_menu` VALUES ('45', '1', '45');
INSERT INTO `sys_role_menu` VALUES ('46', '1', '46');
INSERT INTO `sys_role_menu` VALUES ('47', '2', '1');
INSERT INTO `sys_role_menu` VALUES ('48', '2', '2');
INSERT INTO `sys_role_menu` VALUES ('49', '2', '6');
INSERT INTO `sys_role_menu` VALUES ('50', '2', '13');
INSERT INTO `sys_role_menu` VALUES ('51', '2', '20');
INSERT INTO `sys_role_menu` VALUES ('52', '2', '21');
INSERT INTO `sys_role_menu` VALUES ('53', '2', '25');
INSERT INTO `sys_role_menu` VALUES ('54', '2', '39');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户ID(主键)',
  `username` varchar(50) NOT NULL COMMENT '用户名###{"regex":"^[a-zA-Z][a-zA-Z0-9_-]{1,17}$","note":"必须以字母开头，长度在2~18之间，只能包含字母、数字、下划线、减号"}',
  `password` varchar(50) NOT NULL COMMENT '密码###{"regex":"^[a-zA-Z]\\\\w{5,17}$","note":"必须以字母开头，长度在6~18之间，只能包含字符、数字、下划线"}',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称###{"min":"0","max":"50"}',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机号###{"regex":"^1[3|4|5|7|8]\\\\d{9}$","note":"格式不合法"}',
  `email` varchar(150) DEFAULT NULL COMMENT '邮箱###{"min":"0","max":"150","regex":"^[A-Za-z0-9._%-]+@([A-Za-z0-9-]+\\\\.)+[A-Za-z]{2,4}$","note":"格式不合法"}',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近登陆时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最近登陆IP',
  `user_id_create` int(10) DEFAULT NULL COMMENT '创建用户ID',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态@@@{"0":"锁定","1":"可用"}',
  `gmt_create` datetime DEFAULT NULL COMMENT '录入时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
-- password:root123456
INSERT INTO `sys_user` VALUES ('1', 'root', '11a4091515c32a07247e8df58d54c8bc', 'root', null, null, null, null, null, '1', null, null);
-- password:admin123456
INSERT INTO `sys_user` VALUES ('2', 'admin', '9096dd2f22f34fa712d37ea60ebea557', '管理员', null, null, null, null, null, '1', null, null);
-- password:op123456
INSERT INTO `sys_user` VALUES ('3', 'luffy', 'ebdc8fc60f717a4e9f1a700a9a3ff524', '路飞', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('4', 'zoro', '8e166cc2eabd57bcc6eca7d18afae24a', '索隆', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('5', 'nami', '7fd0077dfe6c293840133b805fd33363', '娜美', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('6', 'usopp', '2e968686f924c45fd8a8911872f7d187', '乌索普', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('7', 'sanji', '00ddda083ded83fd3a7aa64fac184c52', '山治', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('8', 'chopper', 'bb2c9a8cc86d7f31780056db0f9eca17', '乔巴', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('9', 'robin', 'a04eec2458201fd8b7892f987d9506d4', '罗宾', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('10', 'franky', '0f6bca82cb7e738ff08e0cf89b578bf6', '弗兰奇', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('11', 'burukku', 'e95f745912d39d4e7796db38d754846c', '布鲁克', null, null, null, null, null, '1', null, null);
INSERT INTO `sys_user` VALUES ('12', 'jinbe', 'a67673d73bfc123557eac5c895cfd6e7', '甚平', null, null, null, null, null, '1', null, null);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `role_id` int(10) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户_角色_关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2', '1');
INSERT INTO `sys_user_role` VALUES ('3', '3', '2');
INSERT INTO `sys_user_role` VALUES ('4', '4', '2');
INSERT INTO `sys_user_role` VALUES ('5', '5', '2');
INSERT INTO `sys_user_role` VALUES ('6', '6', '2');
INSERT INTO `sys_user_role` VALUES ('7', '7', '2');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `job_class` varchar(100) NOT NULL COMMENT '执行类###{"min":"0","max":"100"}',
  `cron_expression` varchar(100) NOT NULL COMMENT 'cron表达式###{"min":"0","max":"100"}',
  `description` varchar(250) DEFAULT NULL COMMENT '描述###{"min":"0","max":"250"}',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态@@@{"0":"关闭","1":"启动"}',
  `gmt_create` datetime DEFAULT NULL COMMENT '录入时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最近修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES ('1', 'com.cit.web.common.jobs.WelcomeJob', '0 */1 * * * ?', '', '0', null, null);

SET FOREIGN_KEY_CHECKS=1;
