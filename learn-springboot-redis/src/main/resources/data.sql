-- 用户
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_code VARCHAR(250) NOT NULL,
  user_name VARCHAR(250) NOT NULL,
  create_time TIMESTAMP NOT NULL
);
-- 测试数据
INSERT INTO t_user(user_code, user_name, create_time) VALUES
('001', '汪小成', NOW()),
('002', '史小丹', NOW()),
('003', '棒棒', NOW()),
('004', '懒懒', NOW());
