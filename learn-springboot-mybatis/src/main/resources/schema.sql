SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_transfer_log
-- ----------------------------
DROP TABLE IF EXISTS `t_transfer_log`;
CREATE TABLE `t_transfer_log` (
    `id` varchar(64) NOT NULL COMMENT '主键',
    `amount` decimal(10,2) NOT NULL COMMENT '转账金额',
    `transfer_date` varchar(32) NOT NULL COMMENT '转账时间',
    `transfer_type` varchar(8) NOT NULL COMMENT '转账类型',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='银证转账记录';

SET FOREIGN_KEY_CHECKS = 1;