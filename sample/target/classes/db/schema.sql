CREATE TABLE `log_table` (
  `log_id` bigint AUTO_INCREMENT NOT NULL COMMENT '日志类型',
  `type` varchar(255) DEFAULT NULL COMMENT '日志类型',
  `title` varchar(255) DEFAULT NULL COMMENT '日志标题',
  `remote_addr` varchar(20) DEFAULT NULL COMMENT '请求地址',
  `request_uri` varchar(300) DEFAULT NULL COMMENT 'URI',
  `method` varchar(300) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(300) DEFAULT NULL COMMENT '请求参数',
  `exception` varchar(500) DEFAULT NULL COMMENT '异常',
  `operate_date` datetime DEFAULT NULL COMMENT '操作时间',
  `user_id` varchar(30) DEFAULT NULL COMMENT 'id',
  `timeout` varchar(30) DEFAULT NULL COMMENT '操作时长',
  PRIMARY KEY (`log_id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID' ,
  `account` varchar(255) NOT NULL COMMENT '用户账号',
  `password` varchar(255) NOT NULL COMMENT '用户密码',
  `name` varchar(255) NOT NULL COMMENT '用户名称',
  `privilege` int(11) NOT NULL COMMENT '用户权限',
  PRIMARY KEY(`id`)
)DEFAULT CHARSET=utf8;

CREATE TABLE `goods`(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '货物ID',
  `name` varchar(255) NOT NULL COMMENT '货物名称',
  `type` varchar(30) NOT NULL COMMENT '货物型号',
  PRIMARY KEY(`id`)
)DEFAULT CHARSET=utf8;