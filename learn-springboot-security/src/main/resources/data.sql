truncate table t_user;

insert into t_user(id, code, name, password, order_no, create_time)
values('1', 'jenny', 'jenny', '$2a$10$EjjcvEwqx2.8/26i/37nzu3pN9.Ld0HU8S24aXv4gp4MJWAJg8VcO', 9999, now());