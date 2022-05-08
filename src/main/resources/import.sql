--courts
INSERT INTO `scheduler`.`courts` (`court_id`, `_active`, `covered`,`created`, `dimension`, `mime_type`, `name`, `object_name`,`_type`,`url`) VALUES (1,1,1,'1651906627659', '20x20', 'image/png', 'Blue court', '1651906627546_IfoRpDP.png', 'clay', 'http://127.0.0.1:9000/scheduler-bucket/1651906627546_IfoRpDP.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin%2F20220507%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20220507T065707Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=d93f9cfcdd2196d414faef4afa1d1d939fab63a9e8b6c9c0845ad25e11f4ed8c');
INSERT INTO `scheduler`.`courts` (`court_id`, `_active`, `covered`,`created`, `dimension`, `mime_type`, `name`, `object_name`,`_type`,`url`) VALUES (2,1,1,'1651906627658', '24x24', 'image/png', 'Red court', '1651906627549_IfoRpDP.png', 'concrete', 'http://127.0.0.1:9000/scheduler-bucket/1651906627546_IfoRpDP.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin%2F20220507%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20220507T065707Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=d93f9cfcdd2196d414faef4afa1d1d939fab63a9e8b6c9c0845ad25e11f4ed8c');

--reservations court 1:

--start: 19.07.2022 18:00h --> 1658246400000
--end:   19.07.2022 19:00h --> 1658250000000
INSERT INTO `scheduler`.`reservations` (`reservation_id`,`start`,`end`, `court_id` ) VALUES (1, '1658246400000', '1658250000000', 1);

--start: 20.07.2022 18:00h --> 1658332800000
--end:   20.07.2022 19:00h --> 1658336400000
INSERT INTO `scheduler`.`reservations` (`reservation_id`,`start`,`end`, `court_id` ) VALUES (2, '1658332800000', '1658336400000', 1);

--test
--start: 18.07.2022 18:00h --> 1658160000000
--end: 18.07.2022 19:00h --> 1658163600000




--INSERT INTO `scheduler`.`courts` (`court_id`, `_active`,`created`, `mime_type`, `name`, `object_name`,`url`) VALUES (2,1,'1651906627659', 'image/png', 'Red court', '1651906627546_IfoRpDP.png', 'http://127.0.0.1:9000/scheduler-bucket/1651906627546_IfoRpDP.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin%2F20220507%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20220507T065707Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=d93f9cfcdd2196d414faef4afa1d1d939fab63a9e8b6c9c0845ad25e11f4ed8c');

--INSERT INTO `scheduler`.`reservations` (`reservation_id`, `end`, `paid`, `paid_at`, `payment`, `payment_method`, `start`, `user_id`, `court_id`)
--VALUES (1, '1670947200000', 1, '123123', 123.33, 'CASH', '1670860800000', 1, 1);