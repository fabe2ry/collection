-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2018-10-10 04:34:25
-- 服务器版本： 10.1.35-MariaDB
-- PHP 版本： 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `collection_database`
--

-- --------------------------------------------------------

--
-- 表的结构 `goods`
--

CREATE TABLE `goods` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `log_table`
--

CREATE TABLE `log_table` (
  `log_id` int(11) NOT NULL COMMENT '日志主键',
  `type` varchar(255) DEFAULT NULL COMMENT '日志类型',
  `title` varchar(255) DEFAULT NULL COMMENT '日志标题',
  `remote_addr` varchar(20) DEFAULT NULL COMMENT '请求地址',
  `request_uri` varchar(300) DEFAULT NULL COMMENT 'URI',
  `method` varchar(300) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(300) DEFAULT NULL COMMENT '请求参数',
  `exception` varchar(500) DEFAULT NULL COMMENT '异常',
  `operate_date` datetime DEFAULT NULL COMMENT '操作时间',
  `user_id` varchar(30) DEFAULT NULL COMMENT '用户idid',
  `timeout` varchar(30) DEFAULT NULL COMMENT '操作时长'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `log_table`
--

INSERT INTO `log_table` (`log_id`, `type`, `title`, `remote_addr`, `request_uri`, `method`, `params`, `exception`, `operate_date`, `user_id`, `timeout`) VALUES
(15, 'INFO', '登陆', '0:0:0:0:0:0:0:1', '/api/user/login', 'POST', 'account=alice&password=alice', NULL, '2018-09-20 15:43:21', 'alice', '4'),
(16, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/0/5', 'GET', '', NULL, '2018-09-20 15:43:22', 'alice', '6'),
(17, 'INFO', '删除用户', '0:0:0:0:0:0:0:1', '/api/user/delete', 'POST', 'account=php', NULL, '2018-09-20 15:43:24', 'alice', '99'),
(18, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/0/5', 'GET', '', NULL, '2018-09-20 15:43:25', 'alice', '8'),
(19, 'INFO', '修改用户', '0:0:0:0:0:0:0:1', '/api/user/update', 'POST', 'account=go&password=go&name=gogo', NULL, '2018-09-20 15:43:31', 'alice', '131'),
(20, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/0/5', 'GET', '', NULL, '2018-09-20 15:43:32', 'alice', '9'),
(21, 'INFO', '添加用户', '0:0:0:0:0:0:0:1', '/api/user/add', 'POST', 'account=php&password=php&name=php&privilege=1', NULL, '2018-09-20 15:43:38', 'alice', '31'),
(22, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/0/5', 'GET', '', NULL, '2018-09-20 15:43:39', 'alice', '5'),
(23, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/3/5', 'GET', '', NULL, '2018-09-20 15:43:40', 'alice', '5'),
(24, 'INFO', '注销', '0:0:0:0:0:0:0:1', '/api/user/logout', 'GET', '', NULL, '2018-09-20 15:43:42', 'alice', '1'),
(25, 'ERROR', '测试', '0:0:0:0:0:0:0:1', '/api/user/test', 'GET', '', 'test', '2018-09-20 15:44:18', NULL, '2'),
(26, 'INFO', '登陆', '0:0:0:0:0:0:0:1', '/api/user/login', 'POST', 'account=alice&password=alice', NULL, '2018-10-09 11:35:10', NULL, '281'),
(27, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/0/5', 'GET', NULL, NULL, '2018-10-09 11:35:13', 'alice', '97'),
(28, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/0/5', 'GET', NULL, NULL, '2018-10-09 11:35:20', 'alice', '9'),
(29, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/0/5', 'GET', NULL, NULL, '2018-10-09 11:35:21', 'alice', '14'),
(30, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/2/5', 'GET', NULL, NULL, '2018-10-09 11:50:05', NULL, '0'),
(31, 'INFO', '获取列表', '0:0:0:0:0:0:0:1', '/api/user/getUsers/0/5', 'GET', NULL, NULL, '2018-10-09 11:50:12', NULL, '1'),
(32, 'INFO', '注销', '0:0:0:0:0:0:0:1', '/api/user/logout', 'GET', NULL, NULL, '2018-10-09 11:50:15', NULL, '1');

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `privilege` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `account`, `password`, `name`, `privilege`) VALUES
(8, 'go', 'go', 'gogo', 1),
(9, 'golang', 'golang', 'golang', 1),
(10, 'basic', 'basic', 'basic', 1),
(11, 'linux', 'linux', 'linux', 1),
(12, 'virtualbox', 'virtualbox', 'virtualbox', 1),
(13, 'windows', 'windows', 'windows', 1),
(14, 'xammp', 'xammp', 'xammp', 1),
(15, 'sourceinsgiht', 'sourceinsgiht', 'sourceinsgiht', 1),
(23, 'alice', 'alice', 'alice', 2),
(24, '3', '123123', '3', 4),
(25, 'test-add', 'test-add', 'test-add', 1),
(26, 'testadd', 'testadd', 'adfasdfa', 11),
(27, 'ted', 'ted', 'ted', 1),
(28, 'php', 'php', 'php', 1);

--
-- 转储表的索引
--

--
-- 表的索引 `goods`
--
ALTER TABLE `goods`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `log_table`
--
ALTER TABLE `log_table`
  ADD PRIMARY KEY (`log_id`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `goods`
--
ALTER TABLE `goods`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `log_table`
--
ALTER TABLE `log_table`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志主键', AUTO_INCREMENT=33;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
