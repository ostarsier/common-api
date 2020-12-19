-- ----------------------------
-- Table structure for common_api_config
-- ----------------------------
DROP TABLE IF EXISTS `common_api_config`;
CREATE TABLE `common_api_config` (
  `id` varchar(50) NOT NULL COMMENT '内码',
  `api_code` varchar(255) DEFAULT NULL COMMENT '接口编码',
  `api_name` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `note` varchar(255) DEFAULT NULL COMMENT '说明',
  `ds_id` varchar(50) DEFAULT NULL COMMENT '数据源',
  `sql_txt` text COMMENT 'sql',
  `where_txt` text COMMENT '条件',
  `filed_note` text COMMENT '查询结果字段对应中文备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of common_api_config
-- ----------------------------
BEGIN;
INSERT INTO `common_api_config` VALUES ('675cc4e7024b72b8113e16d8afea663b', 'selectStudent', '查询学生', NULL, 'b1e5220c4a0847d9956fb8cc06844f1d', 'select name from student where 1=1 [$age]', '{\"age\":{\"express\":\"[$age]\",\"filed\":\"age\",\"match\":\"=\",\"type\":\"数字\"}}', '{\"name\":\"姓名\"}', '2020-12-19 15:40:27', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for common_api_datasource
-- ----------------------------
DROP TABLE IF EXISTS `common_api_datasource`;
CREATE TABLE `common_api_datasource` (
  `id` varchar(50) NOT NULL COMMENT '内码',
  `name` varchar(255) DEFAULT NULL COMMENT '数据源名称',
  `note` varchar(255) DEFAULT NULL COMMENT '说明',
  `url` varchar(255) DEFAULT NULL COMMENT '数据库链接',
  `type` varchar(255) DEFAULT NULL COMMENT '数据库类型',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of common_api_datasource
-- ----------------------------
BEGIN;
INSERT INTO `common_api_datasource` VALUES ('b1e5220c4a0847d9956fb8cc06844f1d', 'local', NULL, 'jdbc:mysql://localhost:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false', 'mysql', 'root', '123456', '2020-12-19 15:38:59', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for common_api_exc_log
-- ----------------------------
DROP TABLE IF EXISTS `common_api_exc_log`;
CREATE TABLE `common_api_exc_log` (
  `id` bigint(20) NOT NULL COMMENT '内码',
  `api_code` varchar(255) DEFAULT NULL COMMENT '接口编码',
  `exc_sql` text COMMENT '执行sql',
  `exc_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '执行时间',
  `exc_count` int(8) DEFAULT NULL COMMENT '执行次数',
  `exc_msg` text COMMENT '执行信息',
  `exc_param` varchar(255) DEFAULT NULL COMMENT '执行参数',
  `filed1` varchar(255) DEFAULT NULL COMMENT '备用字段1',
  `filed2` varchar(255) DEFAULT NULL COMMENT '备用字段2',
  `filed3` varchar(255) DEFAULT NULL COMMENT '备用字段3',
  `filed4` varchar(255) DEFAULT NULL COMMENT '备用字段4',
  `filed5` varchar(255) DEFAULT NULL COMMENT '备用字段5',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for common_api_log
-- ----------------------------
DROP TABLE IF EXISTS `common_api_log`;
CREATE TABLE `common_api_log` (
  `id` bigint(20) NOT NULL COMMENT '内码',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  `api_name` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `visit_time` datetime DEFAULT NULL COMMENT '访问时间',
  `api_type` varchar(255) DEFAULT NULL COMMENT '接口类型',
  `url` varchar(255) DEFAULT NULL COMMENT '访问路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `age` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
