truncate table t_transfer_log;

insert into t_transfer_log(id, amount, transfer_date, transfer_type, create_time) values
(1, 6000.00, '2020-08-06', 1, now()),
(2, 6009.26, '2020-09-04', 2, now()),
(3, 6400.00, '2020-09-09', 1, now()),
(4, 6758.07, '2020-09-16', 2, now()),
(5, 5400.00, '2020-09-18', 1, now()),
(6, 100.00, '2020-11-25', 1, now()),
(7, 7000.00, '2020-12-14', 1, now()),
(8, 6000.00, '2020-12-29', 1, now()),
(9, 2000.00, '2021-02-01', 1, now());