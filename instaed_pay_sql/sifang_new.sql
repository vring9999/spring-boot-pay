/*
 Navicat Premium Data Transfer

 Source Server         : sifang_new
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 156.227.6.132:3306
 Source Schema         : sifang_new

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 22/05/2020 09:18:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for instead_common_config
-- ----------------------------
DROP TABLE IF EXISTS `instead_common_config`;
CREATE TABLE `instead_common_config`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `CFG_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置名称',
  `CFG_KEY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性名',
  `CFG_VALUE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '属性值',
  `CFG_REMARK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `CFG_TYPE` int(4) NULL DEFAULT NULL COMMENT '1:普通配置   2：秘钥配置',
  PRIMARY KEY (`id`, `CFG_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_common_config
-- ----------------------------
INSERT INTO `instead_common_config` VALUES ('14286990-7dc9-481f-a9af-a804fa4de798', 'aliyun.properties', 'sms.temp.login.check', 'SMS_156515005', '阿里云短信验证模板', 1);
INSERT INTO `instead_common_config` VALUES ('1f1c7966-3f79-4779-a965-aad910c27e67', 'qiniu.properties', 'qiniu.path', 'http://qny.hrcx.top/', '', 2);
INSERT INTO `instead_common_config` VALUES ('3c102c9a-717f-4636-bbd6-0c0eba3727e8', 'system.properties', 'web.base.url', 'http://156.227.6.132:80/instead-pay-web', '我方回调地址', 1);
INSERT INTO `instead_common_config` VALUES ('4b9b8d1c-7812-4170-828f-291fd2822089', 'superpay.properties', 'userPayUrl', ' https://manage.xckjpays.com/index/pay', '代收地址', 1);
INSERT INTO `instead_common_config` VALUES ('5654be15-2155-4f8e-bd55-1690ef361524', 'qiniu.properties', 'qiniu.secretKey', '**********', '', 2);
INSERT INTO `instead_common_config` VALUES ('6548458454445444455454541', 'superpay.properties', 'queryUrl', 'https://www.caishen010.com/index/order_query', '订单查询地址', 1);
INSERT INTO `instead_common_config` VALUES ('6d4b31cc-b8ed-418f-be60-e06d74e43841', 'superpay.properties', 'privateKey', '8d510d80cc7a6dc9c927a0245decb5a0', '签名私钥', 2);
INSERT INTO `instead_common_config` VALUES ('7bddc5f1-7442-4842-a7f4-cf572b2b06d5', 'superpay.properties', 'systemPayUrl', 'https://manage.xckjpays.com/Repay/repayApply', '代付地址', 1);
INSERT INTO `instead_common_config` VALUES ('7d7deaaa-0e75-45e7-a7b3-1d4662c4e704', 'qiniu.properties', 'qiniu.accessKey', '8QY_QmT3GahZlU578HfIJykEXmIKFlrUyR158djL', '', 2);
INSERT INTO `instead_common_config` VALUES ('91c94b94-ef16-4b9d-bb20-c21196ecbdcc', 'qiniu.properties', 'qiniu.bucket', 'hrcxloanspmkt', '', 1);
INSERT INTO `instead_common_config` VALUES ('97f2c00a-d9ae-4f7c-9a6d-1eb93036e808', 'aliyun.properties', 'sms.access.secret', '31vDwnYztMKaR0Ox2VW08mmxzprt0Q', '阿里云短信access_secret', 2);
INSERT INTO `instead_common_config` VALUES ('9ea1f239-cf7c-4a30-b834-b504cd1ac831', 'aliyun.properties', 'sms.access.id', 'LTAIlFfybTARt2Ek', '阿里云短信access_id', 1);
INSERT INTO `instead_common_config` VALUES ('afd84c0b-ba0a-4578-83d7-b5c4af62bff0', 'superpay.properties', 'app_id', 'bb29ab97177e7cbe6dfa6c6698a624e6', '', 2);
INSERT INTO `instead_common_config` VALUES ('bcc9ace7-52b2-4e1f-aeb0-3b71a859ed24', 'aliyun.properties', 'sms.sign.name.comp', '创享网络', '公司签名', 1);
INSERT INTO `instead_common_config` VALUES ('deef995c-bfe6-4946-84a3-d7ed0aa875e2', 'instead.commercial', 'bus.ratio', '0.0002', '商户业务服务比例', 1);
INSERT INTO `instead_common_config` VALUES ('ffa3a924-9db3-4194-98fa-44dc854f243e', 'qiniu.properties', 'ACCESS_KEY', 'PwWECnpuv8UQOLuSlK2ZQ5XW6_jD3vzBxi3SePDb', '七牛云ACCESS_KEY', 2);
INSERT INTO `instead_common_config` VALUES ('ffa3a924-9db3-4194-9zfa-44dc854f243e', 'qiniu.properties', 'HOST_NAME', 'http://qny.iezze.cn/', '七牛云域名', 1);
INSERT INTO `instead_common_config` VALUES ('ffa3a924-9db3-419a-98fa-44dc854f243e', 'qiniu.properties', 'SECRET_KEY', 'p38RQMumeDscfQfXQH9ukPcwr1Zcl3u7TCdHKXNk', '七牛云SECRET_KEY', 1);
INSERT INTO `instead_common_config` VALUES ('ffa3a924-9db3-4594-98fa-44dc854f243e', 'instead.commercial', 'with.ratio', '0.0002', '商户提成服务比例', 1);
INSERT INTO `instead_common_config` VALUES ('ffa3a924-9dbh-4194-98fa-44dc854f243e', 'qiniu.properties', 'BUCKET_NAME', 'bzyj', '七牛云BUCKET_NAME', 1);

-- ----------------------------
-- Table structure for instead_pay_app
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_app`;
CREATE TABLE `instead_pay_app`  (
  `app_id` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'APPID',
  `commercial_number` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商务号',
  `app_name` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用名',
  `app_img` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用logo',
  `app_white_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '白名单',
  `app_back_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `app_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'APPKEY',
  `app_isPut` int(4) NULL DEFAULT 0 COMMENT '是否上架  0：上架  1：下架',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `operator_name` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `operator_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段1',
  `remark2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段2',
  PRIMARY KEY (`app_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_app
-- ----------------------------
INSERT INTO `instead_pay_app` VALUES ('GYN_81812', '2020042418570902', 'test', '', '', '', 'xNLBGhfPz6ql', 0, '2020-04-25 02:59:34', NULL, '2020-04-25 16:45:16', NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('HOX_73702', '2020042302582906', '现金花花', '', '', 'http://www.wedline.cn/huahua/pointPayCallBack', 'Ge4g2KO6q1TG', 0, '2020-04-23 17:38:02', NULL, NULL, NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('HQU_42407', '2020042309354408', '123', '', '', '121', 'E48npIzqzfmA', 0, '2020-04-23 17:38:36', NULL, '2020-04-24 19:18:51', NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('ILC_77074', '2020042306481007', '金乐钱包', 'http://qny.hrcx.top//20200423081503912', '', 'http://www.wuzhijie.cn/orange/bPayController/pointPayCallBack', 'YuxWpf9pO1JW', 0, '2020-04-23 16:20:28', NULL, NULL, NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('NSA_48187', '2020042311121609', '点点秒借', '', '', '', 'XQmKBpbKbz2G', 0, '2020-04-23 19:13:35', NULL, NULL, NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('OUE_27194', '2020042302503202', '111', '', '127.0.0.1', '', 'lLJdoaFQpPuz', 1, '2020-04-23 11:17:10', NULL, '2020-04-24 19:56:59', NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('POK_28124', '2020042212535801', '测试001', '', '', 'http://120.24.237.221:80/karen-beauty-industry/codeController/receive', '7vLFRt73Yz1q', 0, '2020-04-22 21:02:05', NULL, NULL, NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('PPE_61108', '0000000000', '999', '', '', '', 'xxC8ue1OZizn', 0, '2020-04-24 16:54:20', NULL, '2020-04-24 22:15:13', NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('SRD_26911', '0000000000', '888', '', '', '', 'qsILFR2ZLikE', 1, '2020-04-24 16:54:32', NULL, '2020-04-24 19:24:54', NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('UEW_98762', '2020042302521904', '01', '', '', '', 'WZpLfqYZvgaX', 0, '2020-04-23 11:32:42', NULL, NULL, NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('YJA_89238', '2020042403103001', '今日钱庄', '', '', '', 'WKJegkMAWodW', 0, '2020-04-24 14:45:24', NULL, NULL, NULL, NULL);
INSERT INTO `instead_pay_app` VALUES ('YTR_29375', '2020042302522105', '瑞幸钱包', 'http://qny.hrcx.top//20200423030156375', '', 'http://www.zhubianli.cn/ruix/bPayController/pointPayCallBack', 'zNLti8pFjeY5', 0, '2020-04-23 11:03:31', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for instead_pay_commercial
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_commercial`;
CREATE TABLE `instead_pay_commercial`  (
  `commercial_id` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户id',
  `commercial_name` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名',
  `commercial_iphone` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户手机号',
  `commercial_number` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `commercial_password` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户密码',
  `commercial_balance` int(11) NULL DEFAULT 0 COMMENT '商户余额',
  `commercial_ratio` float(11, 4) NULL DEFAULT 0.0000 COMMENT '商户比例',
  `commercial_with_ratio` float(11, 4) NULL DEFAULT 0.0000 COMMENT '商户提现比例',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `creation_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `role_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
  `reserved2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '标识',
  `freeze_money` int(11) NULL DEFAULT 0 COMMENT '冻结金额',
  `all_money` int(11) NULL DEFAULT 0 COMMENT '总金额',
  `all_wit_money` int(11) NULL DEFAULT 0 COMMENT '总提现金额',
  `safety_pwd` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安全密码',
  PRIMARY KEY (`commercial_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_commercial
-- ----------------------------
INSERT INTO `instead_pay_commercial` VALUES ('21312312312312313123', 'oho', '198298765432', '0000000000', '04b672346a2392da17089182e7b00478792b24247224ad5d', 222, 0.2000, 0.0000, '2020-04-23 10:56:37', '2020-02-04 09:56:27', '1', '测试', 0, 0, 0, NULL);
INSERT INTO `instead_pay_commercial` VALUES ('328f1eb4-7a7f-47cd-813b-253c6ec89118', '陈舒熙', '', '0000000000', '063722a87016f9142a195a04386d2219d628d86112271927', 0, 0.0000, 0.0000, '2020-04-23 10:50:18', '2020-04-23 10:50:18', '1', '测试', 0, 0, 0, NULL);
INSERT INTO `instead_pay_commercial` VALUES ('4ed5131b-89d7-4377-bc14-978f4566491e', 'merchant', '19999999999', '2020042418570902', 'b7d55c53cd57d88a39a67574489415c6231cf2759f17b10a', 201528, 0.0170, 0.0200, '2020-04-25 02:57:09', '2020-04-25 02:57:09', '2', '123', 0, 204830, 0, NULL);
INSERT INTO `instead_pay_commercial` VALUES ('56f8bc3a-7e23-42a0-a889-5c72e6cc8024', 'admin', '188291872656', '0000000000', 'f95f34890934278765f2d310a75306f0708d65f01ff4790a', 0, 0.0000, 0.0000, '2020-02-17 10:26:03', '2020-02-17 10:26:03', '1', '测试', 0, 0, 0, NULL);
INSERT INTO `instead_pay_commercial` VALUES ('5cd618cf-f969-456e-85e1-cf6c1ec8cdea', '陈舒楠', '', '0000000000', '444783e8946d97e952789727c3d43a55b227b3b137465b19', 0, 0.0000, 0.0000, '2020-04-23 10:51:03', '2020-04-23 10:51:03', '1', NULL, 0, 0, 0, NULL);
INSERT INTO `instead_pay_commercial` VALUES ('8f3ade05-0cac-469b-96b0-e30594c8fabb', 'admin123', '', '0000000000', '07117153655713c96211bf36b7716466843810be1303dd9e', 0, 0.0000, 0.0000, '2020-05-19 17:57:44', '2020-05-19 17:57:44', '1', '', 0, 0, 0, NULL);
INSERT INTO `instead_pay_commercial` VALUES ('cfd2b45f-573d-4510-ae95-4972c011e64d', '商户测试', '', '2020042302521904', '23659896ba4574c226086c9359621cc09677441746928b7b', 26938928, 0.0170, 0.0200, '2020-04-25 01:04:43', '2020-04-23 10:52:20', '2', NULL, 3051000, 3217679, 158000, NULL);

-- ----------------------------
-- Table structure for instead_pay_menu
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_menu`;
CREATE TABLE `instead_pay_menu`  (
  `menu_id` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `parent_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级菜单id',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名',
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `url_pre` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_menu
-- ----------------------------
INSERT INTO `instead_pay_menu` VALUES ('08d3a959-3cbc-41e5-8b4b-f175a0cc86da', '系统管理', '/6', '路由', '2019-12-13 15:35:40', '1hj', NULL);
INSERT INTO `instead_pay_menu` VALUES ('2d4c329f-ed9f-4e86-a37b-425378d2c3eb', 'root', '收款方式管理', '/qr', '2020-03-05 16:38:30', '', NULL);
INSERT INTO `instead_pay_menu` VALUES ('35b8527d-7dfc-4206-b01f-de816bf3152f', 'root', '订单管理', '/order', '2020-03-05 16:38:45', '', NULL);
INSERT INTO `instead_pay_menu` VALUES ('a7f0c316-ab57-4f2a-880a-d83b3e912dcd', 'root', '菜单管理', '/menu', '2020-03-05 16:37:25', '', NULL);
INSERT INTO `instead_pay_menu` VALUES ('aef9c19c-a9f5-4a7d-8248-1bc177aa7280', 'root', '用户管理', '/commercial', '2020-03-05 16:38:12', '', NULL);
INSERT INTO `instead_pay_menu` VALUES ('da14dd69-dc09-409b-bbab-4a5cbb95fe18', 'root', '配置管理', '/config', '2020-03-05 16:37:45', '', NULL);

-- ----------------------------
-- Table structure for instead_pay_order
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_order`;
CREATE TABLE `instead_pay_order`  (
  `order_Id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '我方单号',
  `out_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部订单',
  `commercial_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `float_money` int(10) NULL DEFAULT NULL COMMENT '浮动金额',
  `operator_money` int(10) NULL DEFAULT 0 COMMENT '操作金额',
  `deducted_money` int(11) NULL DEFAULT NULL COMMENT '扣除手续费后的金额',
  `qr_Id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码id',
  `maker_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打款人姓名',
  `create_Time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `confirm_time` datetime(0) NULL DEFAULT NULL COMMENT '确认时间',
  `operator_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人（系统管理员）',
  `application_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `num` int(11) NULL DEFAULT 1 COMMENT '数量',
  `application_type` int(11) NULL DEFAULT 1 COMMENT '类型     1代收   2代付   3提现    4充值',
  `pay_type` int(11) NULL DEFAULT NULL COMMENT '支付方式   1 支付宝   3微信   2银行卡',
  `order_status` int(11) NULL DEFAULT NULL COMMENT '订单状态  0 已关闭取消  1已承兑  2待承兑   3已过期（异常关闭）',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户--订单标识',
  `is_hand` int(11) NULL DEFAULT NULL COMMENT '是否为手动 1是  2否',
  `call_back_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `call_back_num` int(5) NULL DEFAULT 5 COMMENT '回调次数',
  `bitcoin` float(10, 2) NULL DEFAULT 0.00 COMMENT '比特币',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流水号',
  `remit_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款地址',
  `bank_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡号',
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款银行',
  `bank_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款银行开户行分行',
  `bank_user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款人名字',
  `bank_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款人联系方式',
  `remark4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现拒绝原因',
  `our_ratio` int(11) NULL DEFAULT 0 COMMENT '我方扣除的服务费',
  `back_status` int(11) NULL DEFAULT 0 COMMENT '商户回调状态 0未回调 1 回调成功   2回调失败',
  `back_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商户回调信息',
  `line` int(5) NULL DEFAULT 0 COMMENT '是否走线下承兑    1是  0 否',
  PRIMARY KEY (`order_Id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_order
-- ----------------------------
INSERT INTO `instead_pay_order` VALUES ('jnw_in_657794', 'Bgi9QKsSGKfX', '2020042418570902', 15000, 15000, 14746, NULL, '', '2020-05-19 16:01:49', '2020-05-19 18:03:44', NULL, 'GYN_81812', 1, 1, 2, 1, '蔡志强', 1, NULL, 0, 0.00, '2020051916024075172', 'https://manage.xckjpays.com/index/order.html?sys_order_no=ZtBbQGfVHpg9A6FfQKW8SEe8oEAePjpsDnpfadjMPgk=', '', '', '', '', '', NULL, 254, 1, NULL, 0);
INSERT INTO `instead_pay_order` VALUES ('rky_out_469922', 'jB1Qm5M4P47g', '2020042302521904', 10000, 10000, 10170, NULL, '', '2020-05-19 17:55:13', '2020-05-19 18:11:29', NULL, 'UEW_98762', 1, 2, 2, 1, '蔡志强', 1, NULL, 0, 0.00, NULL, NULL, '6217001820011698267', '中国建设银行', '', '蔡志强', '', NULL, 170, 0, NULL, 0);
INSERT INTO `instead_pay_order` VALUES ('wla_cz_159154', '9761506186', '2020042302521904', NULL, 100, 98, NULL, '商户测试', '2020-05-19 17:43:54', '2020-05-19 17:43:54', '陈舒楠', NULL, 1, 4, 2, 1, NULL, 1, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 0, NULL, NULL);

-- ----------------------------
-- Table structure for instead_pay_percentage
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_percentage`;
CREATE TABLE `instead_pay_percentage`  (
  `percentage_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单id',
  `commercial_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `collection_money` float(11, 2) NULL DEFAULT 0.00 COMMENT '代收金额',
  `collection_type` int(11) NULL DEFAULT NULL COMMENT '服务类型',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`percentage_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_percentage
-- ----------------------------
INSERT INTO `instead_pay_percentage` VALUES ('07db69a0-dac8-4e06-8718-4539e229a8a0', 'beh_in_669766', '2020042302522105', 5126.00, 1, '0', '2020-04-23 16:20:00');
INSERT INTO `instead_pay_percentage` VALUES ('12121', '1212', '1212', 909090.00, 1, '', '2019-12-12 12:00:00');
INSERT INTO `instead_pay_percentage` VALUES ('15b3216b-d9d4-4929-8ee6-ad612369a031', 'evk_in_892010', '2020042302522105', 3400.00, 1, '0', '2020-04-23 14:02:57');
INSERT INTO `instead_pay_percentage` VALUES ('1688b960-1a0d-4bac-9678-d3094c4f5fe7', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:55:01');
INSERT INTO `instead_pay_percentage` VALUES ('17c70cb8-7c2b-4aec-aacf-634fcd5ceaf6', 'xuv_out_821590', '2020042302522105', 170.00, 2, '0', '2020-04-23 16:04:29');
INSERT INTO `instead_pay_percentage` VALUES ('18b71c77-aceb-4288-87e9-35707db81ff5', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:55:02');
INSERT INTO `instead_pay_percentage` VALUES ('1a36c1b5-7b77-4d10-acec-925399a58957', '6362df0f-0445-421f-8843-bfcec68c6e26', '2020021411353201', 0.00, 3, '0', '2020-02-26 09:54:50');
INSERT INTO `instead_pay_percentage` VALUES ('1bb23cba-2f11-4253-9e49-75e015969823', 'jwz_in_442619', '2019121915570603', 522689.00, 1, '0', '2019-12-28 14:45:03');
INSERT INTO `instead_pay_percentage` VALUES ('1c5d819f-8594-48d5-ae91-0ed9fb5de824', '4d46993a-3c0c-48c9-aeae-1f95fc4b966a', '2020021911080301', 117810.00, 3, '0', '2020-04-22 10:24:53');
INSERT INTO `instead_pay_percentage` VALUES ('2005a5ac-fac1-4aa8-872c-bbf2f1b8ece1', 'owf_in_831219', '2020021911080301', 1600.00, 1, '0', '2020-02-26 10:08:17');
INSERT INTO `instead_pay_percentage` VALUES ('21d1477e-38d2-4f69-acef-9c0c688af405', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:58:40');
INSERT INTO `instead_pay_percentage` VALUES ('2480216a-4c35-4f16-a901-c325c3a5a79c', 'xuv_out_821590', '2020042302522105', 170.00, 2, '0', '2020-04-23 15:57:54');
INSERT INTO `instead_pay_percentage` VALUES ('24f5e6dd-38f3-4ac6-8eed-917893ae5073', 'qgw_in_339891', '2019121915570603', 9.00, 2, NULL, '2019-12-23 16:04:27');
INSERT INTO `instead_pay_percentage` VALUES ('258e87a8-58fa-44b6-a321-880018c6fd4b', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:38:43');
INSERT INTO `instead_pay_percentage` VALUES ('2a41cb49-45d3-415e-bf9c-06be5a33618e', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 18:03:44');
INSERT INTO `instead_pay_percentage` VALUES ('2c586b19-ea38-43f5-9893-b2412a369170', 'jwz_in_442619', '2019121915570603', 522689.00, 1, '0', '2019-12-28 14:32:53');
INSERT INTO `instead_pay_percentage` VALUES ('2df3d059-27a0-4cc6-82d5-b2210d648c61', 'e3ba5626-7a47-4bf0-ba84-68a647a7d292', '2020042302522105', 406800.00, 3, '0', '2020-04-23 14:40:33');
INSERT INTO `instead_pay_percentage` VALUES ('363cddcf-bb82-41f3-8691-40b4ff207d39', 'xym_in_286694', '2019121915570603', 0.00, 1, '0', '2020-02-17 16:06:26');
INSERT INTO `instead_pay_percentage` VALUES ('3971ef0e-88cb-4c0e-b8d5-b87da92149fb', 'qtj_out_559746', '2020042302582906', 170.00, 2, '0', '2020-04-23 19:54:36');
INSERT INTO `instead_pay_percentage` VALUES ('430e9dce-4232-41b5-be97-d9723d992f05', 'zia_in_439969', '2020021911080301', 200.00, 1, '0', '2020-02-26 10:39:43');
INSERT INTO `instead_pay_percentage` VALUES ('46d903cd-4fa4-48ce-ad4f-cb4c1ec4a88e', 'qgw_in_339891', '2019121915570603', 9.00, 2, NULL, '2019-12-23 16:04:44');
INSERT INTO `instead_pay_percentage` VALUES ('4a87368c-28de-4012-9422-43bafcb6f5c6', 'qgw_in_339891', '2019121915570603', 0.00, 1, '0', '2020-02-17 10:48:45');
INSERT INTO `instead_pay_percentage` VALUES ('4fdac4b7-49fd-4a12-a220-ff520a59c661', 'qgw_in_339891', '2019121915570603', 9.00, 1, NULL, '2019-12-23 16:04:48');
INSERT INTO `instead_pay_percentage` VALUES ('53eeffb5-175d-4e2d-bfd8-7e70fd0dac90', 'xym_in_286694', '2019121915570603', 0.00, 1, '0', '2020-02-17 10:44:53');
INSERT INTO `instead_pay_percentage` VALUES ('5c3c19f0-3bc3-494c-9216-93b222e45cc7', 'jwz_in_442619', '2019121915570603', 24890.00, 1, '0', '2019-12-28 15:04:10');
INSERT INTO `instead_pay_percentage` VALUES ('62eaf441-44aa-45a7-98cf-e5b11412b80c', '52bb2e00-3d04-490a-a331-06ce9995df62', '2020021911080301', 20400.00, 3, '0', '2020-02-20 16:34:32');
INSERT INTO `instead_pay_percentage` VALUES ('654ee09d-c769-43d8-9603-6f4b542645dc', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 16:04:41');
INSERT INTO `instead_pay_percentage` VALUES ('67472ad0-1ce6-4b64-ab81-041e2571bbd2', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:39:04');
INSERT INTO `instead_pay_percentage` VALUES ('69ed1a5e-df73-48bb-9131-ea92dcbcbbdd', 'yao_in_348310', '2020021911080301', 25000.00, 1, '0', '2020-04-22 23:17:01');
INSERT INTO `instead_pay_percentage` VALUES ('6e17099f-37bb-40b9-ba58-3dab2f49f5a1', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:31:54');
INSERT INTO `instead_pay_percentage` VALUES ('70517bc7-4973-4334-8da6-7ed5d0736a73', 'qhr_in_974792', '2020042212535801', 2000.00, 1, '0', '2020-04-22 23:07:32');
INSERT INTO `instead_pay_percentage` VALUES ('73a47451-6858-4234-a001-142fffa8f8ad', 'qgw_in_339891', '2019121915570603', 9.00, 1, NULL, '2019-12-23 16:04:50');
INSERT INTO `instead_pay_percentage` VALUES ('786de651-ac35-451e-86ab-f1df874f94e8', 'prn_in_999869', '2020042302522105', 5126.00, 1, '0', '2020-04-23 16:21:00');
INSERT INTO `instead_pay_percentage` VALUES ('8081323e-b30a-470e-821f-d1d69a328f93', 'qgw_in_339891', '2019121915570603', 9.00, 2, NULL, '2019-12-23 16:04:54');
INSERT INTO `instead_pay_percentage` VALUES ('8573559a-2c22-432c-bd37-9747aefea2be', 'jwz_in_442619', '2019121915570603', 24890.00, 1, '0', '2019-12-28 14:52:43');
INSERT INTO `instead_pay_percentage` VALUES ('978047a2-f28c-4d76-b475-fb1c97051314', 'diq_out_984074', '2020042302522105', 172.00, 2, '0', '2020-04-23 16:17:48');
INSERT INTO `instead_pay_percentage` VALUES ('98fcb1aa-016d-48b8-ba81-5bac4f073f09', 'vsw_in_510028', '2020042212535801', 2000.00, 1, '0', '2020-04-22 23:47:21');
INSERT INTO `instead_pay_percentage` VALUES ('9e8be082-3b90-4a1b-a901-28fb815673b3', 'big_in_837667', '2020042212535801', 20.00, 1, '0', '2020-04-22 23:11:55');
INSERT INTO `instead_pay_percentage` VALUES ('aa9b7e21-b404-4204-8681-621febd0b0e8', 'jwz_in_442619', '2019121915570603', 24890.00, 1, '0', '2019-12-28 14:56:36');
INSERT INTO `instead_pay_percentage` VALUES ('ab09debe-5d60-420d-bfaa-5fe6307f9de4', '44ce71f1-2c19-4dd0-95d4-5e18fc9c9db2', '2020021911080301', 102000.00, 3, '0', '2020-02-26 12:03:16');
INSERT INTO `instead_pay_percentage` VALUES ('acf04096-fd7f-436b-ae59-fc7ee9664173', 'xym_in_286694', '2019121915570603', 0.00, 1, '0', '2020-02-17 10:44:54');
INSERT INTO `instead_pay_percentage` VALUES ('af55bad9-0a69-4015-8b42-4e50d7c4c0d2', 'xuv_out_821590', '2020042302522105', 170.00, 2, '0', '2020-04-23 15:33:36');
INSERT INTO `instead_pay_percentage` VALUES ('b3d30cfc-f67a-4938-b6e7-345c30bd3249', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:56:12');
INSERT INTO `instead_pay_percentage` VALUES ('bc77758f-a59d-41c4-8b36-5034db0f4052', 'rky_out_469922', '2020042302521904', 170.00, 2, '0', '2020-05-19 18:11:29');
INSERT INTO `instead_pay_percentage` VALUES ('c273fb96-1c2f-4e06-be86-c6f71f28621e', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:55:07');
INSERT INTO `instead_pay_percentage` VALUES ('c41366a3-0362-4aed-9ea1-d7e61f4b9e72', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:38:15');
INSERT INTO `instead_pay_percentage` VALUES ('c41a6fda-2152-40aa-b81c-e1e06d44af99', 'unt498621', '2020042302521904', 12000.00, 3, '0', '2020-04-23 19:19:38');
INSERT INTO `instead_pay_percentage` VALUES ('c9d63352-1036-4d3b-9826-25c56e0701da', 'mcu_in_610544', '2020042302522105', 170.00, 1, '0', '2020-04-23 16:15:00');
INSERT INTO `instead_pay_percentage` VALUES ('c9f4bc55-0e98-41d4-b6f7-a2c32c42dace', 'xuv_out_821590', '2020042302522105', 170.00, 2, '0', '2020-04-23 15:57:13');
INSERT INTO `instead_pay_percentage` VALUES ('cbb005b4-5dad-4d64-92e6-78bc2da9718d', 'mpw_in_192859', '2020042302522105', 5126.00, 1, '0', '2020-04-23 13:22:20');
INSERT INTO `instead_pay_percentage` VALUES ('ce14837a-e22d-4028-9353-88cfd568ca77', 'xuv_out_821590', '2020042302522105', 170.00, 2, '0', '2020-04-23 15:56:55');
INSERT INTO `instead_pay_percentage` VALUES ('d9a28108-e3f0-4499-9b4c-4a881a9e4196', 'afx_in_223387', '2019121915570603', 9.00, 1, NULL, '2019-12-23 16:04:58');
INSERT INTO `instead_pay_percentage` VALUES ('e255a4be-649a-47c1-aaf9-1d8f26a60efc', 'jwz_in_442619', '2019121915570603', 522689.00, 1, '0', '2019-12-28 14:46:12');
INSERT INTO `instead_pay_percentage` VALUES ('eff5d9f6-0bf0-420b-9b17-007e6deeb39c', 'qgw_in_339891', '2019121915570603', 9.00, 1, NULL, '2019-12-23 16:05:01');
INSERT INTO `instead_pay_percentage` VALUES ('f1314936-4bf7-4bb9-b79c-73d6a2c6e552', 'qmr_in_198462', '2020021911080301', 25000.00, 1, '0', '2020-04-22 23:15:01');
INSERT INTO `instead_pay_percentage` VALUES ('f2049e11-9785-4b8c-8377-b852e4b468de', 'ksh_in_806692', '2020042302522105', 5126.00, 1, '0', '2020-04-23 16:20:01');
INSERT INTO `instead_pay_percentage` VALUES ('f2605ca6-86cd-4a2f-b758-7bd03df030c2', 'afx_in_223387', '2019121915570603', 9.00, 1, NULL, '2019-12-23 16:05:03');
INSERT INTO `instead_pay_percentage` VALUES ('f3056e3b-d2a4-4396-80b6-5a73176ea377', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:55:00');
INSERT INTO `instead_pay_percentage` VALUES ('f4ce70ee-c7ee-430d-a85d-32e35cb6ea59', 'qgw_in_339891', '2019121915570603', 24381804.00, 1, '0', '2019-12-27 17:18:19');
INSERT INTO `instead_pay_percentage` VALUES ('f4f32cbf-66e7-4631-a200-12a5b8fc12fd', 'jnw_in_657794', '2020042418570902', 254.00, 1, '0', '2020-05-19 17:54:56');
INSERT INTO `instead_pay_percentage` VALUES ('f780648e-1e1a-4adc-bb36-420feced0edb', 'ylo_in_735611', '2020021911080301', 25000.00, 1, '0', '2020-04-22 23:12:01');
INSERT INTO `instead_pay_percentage` VALUES ('f7c0d158-3e5a-4d69-8637-ae76ee45e9bb', 'acdbade6-ffb7-40aa-89af-2456dd66847a', '2020021411353201', 0.00, 3, '0', '2020-02-27 15:16:47');
INSERT INTO `instead_pay_percentage` VALUES ('f9cd9547-9857-4afe-8e6c-d1897a97ab1d', '4ec599da-e06e-4b54-994a-ec2c97890be0', '2020021411353201', 0.00, 3, '0', '2020-02-27 15:12:15');

-- ----------------------------
-- Table structure for instead_pay_qr
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_qr`;
CREATE TABLE `instead_pay_qr`  (
  `qr_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `qr_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码地址',
  `enable_status` int(5) NULL DEFAULT 1 COMMENT '启用状态   0停用   1启用',
  `bank_account` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '账号',
  `Receipt_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款名',
  `Receipt_type` int(5) NULL DEFAULT 1 COMMENT '二维码类型   1 支付宝二维码   2微信二维码   3银行卡   4支付宝账号   5微信账号',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `remark1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用',
  `qr_number` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '排序number',
  `commercial_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  PRIMARY KEY (`qr_number`, `qr_id`) USING BTREE,
  INDEX `fast_enable`(`enable_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_qr
-- ----------------------------
INSERT INTO `instead_pay_qr` VALUES ('2', 'http://qny.hrcx.top/20191028090530604', 0, '1', '红孩儿', 2, '2019-12-19 16:51:35', NULL, NULL, 33, NULL);
INSERT INTO `instead_pay_qr` VALUES ('3', 'http://qny.hrcx.top/20191028090530604', 1, '1', '红孩儿', 1, '2019-12-19 16:52:08', NULL, NULL, 37, NULL);
INSERT INTO `instead_pay_qr` VALUES ('4', 'http://qny.hrcx.top/20191028090530604', 0, '1', '红孩儿', 1, '2019-12-19 16:52:22', NULL, NULL, 38, NULL);
INSERT INTO `instead_pay_qr` VALUES ('1', 'http://qny.hrcx.top/20191025155651885', 1, '1', '红孩儿', 1, '2019-12-19 16:50:43', NULL, NULL, 39, NULL);
INSERT INTO `instead_pay_qr` VALUES ('99a58584-677b-42ae-aef9-820c64f0ea9c', 'http://qny.hrcx.top//20200102161640694', 1, '0', '红孩儿', 1, '2020-01-06 14:44:25', NULL, NULL, 40, NULL);
INSERT INTO `instead_pay_qr` VALUES ('23581c8b-3e4a-4966-80f0-ba5a4809292b', 'http://qny.hrcx.top//20191226162855633', 0, NULL, '1231212,22222222', 1, '2020-01-06 15:02:50', NULL, NULL, 41, NULL);
INSERT INTO `instead_pay_qr` VALUES ('1c72aa51-fee6-4761-94e1-50ac3b91860b', 'http://qny.hrcx.top//20200215152709928', 1, '123123', '2', 1, '2020-02-15 15:27:19', NULL, NULL, 42, NULL);
INSERT INTO `instead_pay_qr` VALUES ('c2d9fe80-bca0-4978-8381-47c5cdecf15d', 'http://qny.hrcx.top//20200224150852451', 1, '蓝山咖啡', 'ccc', 1, '2020-02-24 15:09:11', NULL, NULL, 43, '2020021911080301');
INSERT INTO `instead_pay_qr` VALUES ('5ab75057-06f0-4050-b779-a2006d807756', 'http://qny.hrcx.top//20200224172204422', 1, '123456777', '147', 1, '2020-02-24 17:22:16', NULL, NULL, 44, '2020021411353201');
INSERT INTO `instead_pay_qr` VALUES ('ddd05764-ba2b-4da0-8dc0-e52402a1013f', 'http://qny.hrcx.top//20200225091533765', 0, '', '姜子牙', 1, '2020-02-25 09:15:52', NULL, NULL, 45, '2020021911080301');
INSERT INTO `instead_pay_qr` VALUES ('c5b65a06-c990-497a-9ddf-6caaf6e2946e', 'http://qny.hrcx.top//20200225091612531', 1, '000', 'qq', 1, '2020-02-25 09:16:24', NULL, NULL, 46, '2020021911080301');
INSERT INTO `instead_pay_qr` VALUES ('abaaba0f-7432-40c8-80f0-954fb0b14c10', 'http://qny.hrcx.top//20200225091653533', 1, '111', '11', 2, '2020-02-25 09:17:01', NULL, NULL, 47, '2020021911080301');
INSERT INTO `instead_pay_qr` VALUES ('cc606df5-12e6-4feb-aa82-a34f85e17046', 'http://qny.hrcx.top//20200225092350471', 1, '', '123', 1, '2020-02-25 09:24:15', NULL, NULL, 48, '2020021911080301');
INSERT INTO `instead_pay_qr` VALUES ('2ad4896b-b3cd-4078-b0c1-cbf551eb295b', 'http://qny.hrcx.top//20200226151820621', 0, '', '高渐离', 2, '2020-02-26 15:18:29', NULL, NULL, 51, '0000000000');
INSERT INTO `instead_pay_qr` VALUES ('3c399ea7-6f86-4f02-9322-6066d8d772bc', '', 1, '6217002200031196734', '陈志锋', 3, '2020-04-23 13:44:02', NULL, NULL, 52, '2020042302522105');
INSERT INTO `instead_pay_qr` VALUES ('71553ac3-6b87-4988-9984-9072e498c9b2', '', 0, '6217995540003011119', '雪女', 3, '2020-02-26 15:18:03', NULL, NULL, 53, '0000000000');
INSERT INTO `instead_pay_qr` VALUES ('92a30507-50f4-4f7f-b24f-c35f5a28f57f', 'http://qny.hrcx.top//20200225152849020', 0, '22', '11', 1, '2020-02-25 15:28:57', NULL, NULL, 54, '0000000000');

-- ----------------------------
-- Table structure for instead_pay_role
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_role`;
CREATE TABLE `instead_pay_role`  (
  `role_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role_level` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_role
-- ----------------------------
INSERT INTO `instead_pay_role` VALUES ('1', 'ROLE_ADMIN', 1);
INSERT INTO `instead_pay_role` VALUES ('2', 'ROLE_USER', 2);
INSERT INTO `instead_pay_role` VALUES ('3', 'ROLE_LOGIN', 1);

-- ----------------------------
-- Table structure for instead_pay_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_role_menu`;
CREATE TABLE `instead_pay_role_menu`  (
  `id` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_id` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_role_menu
-- ----------------------------
INSERT INTO `instead_pay_role_menu` VALUES ('1', '1', '1', NULL);
INSERT INTO `instead_pay_role_menu` VALUES ('2', '2', '2', NULL);
INSERT INTO `instead_pay_role_menu` VALUES ('3', '1', '2', NULL);
INSERT INTO `instead_pay_role_menu` VALUES ('4', '1', '3', NULL);
INSERT INTO `instead_pay_role_menu` VALUES ('5', '2', '5', NULL);
INSERT INTO `instead_pay_role_menu` VALUES ('6', '2', '1', NULL);

-- ----------------------------
-- Table structure for instead_pay_token_white
-- ----------------------------
DROP TABLE IF EXISTS `instead_pay_token_white`;
CREATE TABLE `instead_pay_token_white`  (
  `commercial_number` varchar(144) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外网ip',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of instead_pay_token_white
-- ----------------------------
INSERT INTO `instead_pay_token_white` VALUES ('2019121915570603', '106.122.242.11', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '127.0.0.1', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '192.168.0.109', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '192.168.0.115', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '192.168.0.122', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '192.168.0.110', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2019121915570603', '1234', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2019121915570603', '120.36.214.62', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2019121915570603', '127.0.0.1', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2019121915570603', '1.1.1.1', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '127.0.0.1', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '192.168.0.109', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '192.168.0.115', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '192.168.0.122', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '192.168.0.110', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('0000000000', '120.36.214.101', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2020021411353201', '192.168.0.0', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2020022909503403', '103.140.150.20', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2020022909503403', '120.24.237.221', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2020021911080301', '103.140.150.20', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2020021911080301', '120.24.237.221', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2020021911080301', '120.36.214.43', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2020021911080301', '211.97.128.75', NULL);
INSERT INTO `instead_pay_token_white` VALUES ('2020042212535801', '120.36.214.43', NULL);

SET FOREIGN_KEY_CHECKS = 1;
