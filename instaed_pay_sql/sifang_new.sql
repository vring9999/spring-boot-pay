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

SET FOREIGN_KEY_CHECKS = 1;
