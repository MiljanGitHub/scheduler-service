--courts
INSERT INTO `scheduler`.`courts` (`court_id`,`name`,`created`, `_active`) VALUES (1,'teren1' ,'1650113196417', 1);

INSERT INTO `scheduler`.`reservations` (`reservation_id`, `end`, `paid`, `paid_at`, `payment`, `payment_method`, `start`, `user_id`, `court_id`)
VALUES (1, '1670947200000', 1, '123123', 123.33, 'CASH', '1670860800000', 1, 1);