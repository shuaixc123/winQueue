
DROP TABLE IF EXISTS `t_sys_info`;
CREATE TABLE `t_sys_info` (
  `lockNum` int(9) default '0' COMMENT '用于互斥',
  `maxQueue` int(9) default '0',
  `gid` int(9) NOT NULL auto_increment,
  PRIMARY KEY  (`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sys_info
-- ----------------------------
INSERT INTO `t_sys_info` VALUES ('0', '26', '1');

-- ----------------------------
-- Table structure for t_win_info
-- ----------------------------
DROP TABLE IF EXISTS `t_win_info`;
CREATE TABLE `t_win_info` (
  `gid` int(9) NOT NULL auto_increment,
  `windowNo` varchar(1) default NULL,
  `busiTime` decimal(4,1) default NULL,
  PRIMARY KEY  (`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_win_info
-- ----------------------------
INSERT INTO `t_win_info` VALUES ('1', 'A', '1.1');
INSERT INTO `t_win_info` VALUES ('2', 'B', '1.2');
INSERT INTO `t_win_info` VALUES ('3', 'C', '1.3');
INSERT INTO `t_win_info` VALUES ('4', 'D', '1.4');
INSERT INTO `t_win_info` VALUES ('5', 'E', '0.9');
INSERT INTO `t_win_info` VALUES ('6', 'F', '1.0');
INSERT INTO `t_win_info` VALUES ('7', 'G', '1.2');
INSERT INTO `t_win_info` VALUES ('8', 'H', '1.6');
INSERT INTO `t_win_info` VALUES ('9', 'I', '1.8');
INSERT INTO `t_win_info` VALUES ('10', 'J', '1.5');

-- ----------------------------
-- Table structure for t_win_pause
-- ----------------------------
DROP TABLE IF EXISTS `t_win_pause`;
CREATE TABLE `t_win_pause` (
  `gid` int(9) NOT NULL auto_increment,
  `windowNo` varchar(1) default NULL,
  `starttime` decimal(4,1) default NULL,
  `endtime` decimal(4,1) default NULL,
  PRIMARY KEY  (`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_win_pause
-- ----------------------------

-- ----------------------------
-- Table structure for t_win_queue
-- ----------------------------
DROP TABLE IF EXISTS `t_win_queue`;
CREATE TABLE `t_win_queue` (
  `gid` int(9) NOT NULL auto_increment,
  `windowNo` varchar(1) default NULL COMMENT '窗口ID',
  `employeeNo` varchar(50) default NULL COMMENT '业务办理人',
  `taskNum` int(9) default NULL COMMENT '任务数量',
  `startTime` decimal(4,1) default NULL,
  `endTime` decimal(4,1) NOT NULL,
  `queueNo` int(9) default NULL,
  PRIMARY KEY  (`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_win_queue
-- ----------------------------
