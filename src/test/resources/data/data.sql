CREATE TABLE IF NOT EXISTS `address`
(
    `id`        bigint      NOT NULL AUTO_INCREMENT,
    `town_name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `town_name` (`town_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

INSERT INTO `address`
VALUES (3, '상수동'),
       (2, '영통동'),
       (1, '운암동');

CREATE TABLE  IF NOT EXISTS `category`
(
    `id`   bigint      NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

INSERT INTO `category`
VALUES (1, '족발'),
       (2, '치킨');

CREATE TABLE  IF NOT EXISTS  `user`
(
    `id`           varchar(50)  NOT NULL,
    `email`        varchar(50)  NOT NULL,
    `password`     varchar(255) NOT NULL,
    `name`         varchar(30)  NOT NULL,
    `phone_number` varchar(30)  NOT NULL,
    `type`         varchar(20)  NOT NULL,
    `created_at`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

INSERT INTO `user`
VALUES ('rider1', 'test@mail.com', '$2a$10$Uj.pN4fw2AZ1n5eCCtKFiutvieNbHpAhiOdQc4yW/klpm27F6ex32', '라이더1',
        '010-1234-1234', 'RIDER', '2022-08-03 11:58:39', '2022-08-23 12:43:38'),
       ('test123', 'test@mail.com', '$2a$10$G8RDYEOaiCMeyxzio0Rz..CiYkaDksT.1ckd2wMsRK7xUFZEqsjzO', '테스트',
        '010-1234-1234', 'USER', '2022-07-25 10:55:34', '2022-07-25 10:55:34'),
       ('user1', 'user2@naver.com', '$2a$10$Uj.pN4fw2AZ1n5eCCtKFiutvieNbHpAhiOdQc4yW/klpm27F6ex32', '유저1',
        '010-1111-1111', 'USER', '2022-07-20 12:45:01', '2022-07-20 12:45:01'),
       ('user2', 'user2@naver.com', '$2a$10$Uj.pN4fw2AZ1n5eCCtKFiutvieNbHpAhiOdQc4yW/klpm27F6ex32', '유저1',
        '010-1111-1123', 'OWNER', '2022-07-20 12:45:01', '2022-08-18 09:19:12');

CREATE TABLE  IF NOT EXISTS `user_address`
(
    `id`             bigint      NOT NULL AUTO_INCREMENT,
    `user_id`        varchar(50) NOT NULL,
    `address_id`     bigint      NOT NULL,
    `detail_address` varchar(50) NOT NULL,
    `alias`          varchar(50) NOT NULL,
    `selected`       tinyint     NOT NULL DEFAULT '0',
    `created_at`     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY              `user_id` (`user_id`),
    KEY              `address_id` (`address_id`),
    CONSTRAINT `user_address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `user_address_ibfk_2` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8mb3;

INSERT INTO `user_address`
VALUES (15, 'user1', 1, '15번길 13로', '15번길 13로', 0, '2022-07-25 10:56:15', '2022-07-26 13:21:02'),
       (16, 'user1', 1, '15번길 13로 101동 101호', '15번길 13로 101동 101호', 0, '2022-07-25 11:14:23', '2022-07-25 11:16:17'),
       (17, 'user1', 1, '15번길 13로 101동 102호', '15번길 13로 101동 102호', 1, '2022-07-25 11:14:33', '2022-07-26 13:21:02'),
       (18, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-25 11:14:45', '2022-07-26 13:19:53'),
       (60, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:21:01', '2022-07-28 11:21:01'),
       (61, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:21:05', '2022-07-28 11:21:05'),
       (62, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:21:08', '2022-07-28 11:21:08'),
       (63, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:21:56', '2022-07-28 11:21:56'),
       (64, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:23:49', '2022-07-28 11:23:49'),
       (65, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:23:56', '2022-07-28 11:23:56'),
       (66, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:43:04', '2022-07-28 11:43:04'),
       (67, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:43:17', '2022-07-28 11:43:17'),
       (68, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:49:39', '2022-07-28 11:49:39'),
       (69, 'user1', 1, '15번길 13로 101동 103호', '우리집', 0, '2022-07-28 11:51:50', '2022-07-28 11:51:50');

CREATE TABLE  IF NOT EXISTS `store`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT,
    `address_id`     bigint       NOT NULL,
    `user_id`        varchar(50)  NOT NULL,
    `category_id`    bigint       NOT NULL,
    `detail_address` varchar(50)  NOT NULL,
    `name`           varchar(50)  NOT NULL,
    `phone_number`   varchar(50)  NOT NULL,
    `info`           varchar(255) NOT NULL,
    `status`         varchar(20)  NOT NULL DEFAULT 'CLOSED',
    `open_time`      varchar(50)  NOT NULL,
    `end_time`       varchar(50)  NOT NULL,
    `min_price`      bigint       NOT NULL,
    `created_at`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY              `user_id` (`user_id`),
    KEY              `category_id` (`category_id`),
    KEY              `idx_store_address_id_category_id_status` (`address_id`,`category_id`,`status`) /*!80000 INVISIBLE */,
    CONSTRAINT `store_ibfk_1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `store_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `store_ibfk_3` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=1557380 DEFAULT CHARSET=utf8mb3;

INSERT INTO `store`
VALUES (1, 1, 'user2', 1, '매장상세주소 101호', 'BBQ치킨', '010-1111-1111', '치킨집입니다', 'CLOSED', '11:00', '23:00', 18000,
        '2022-07-16 04:35:47', '2022-07-26 11:30:30'),
       (2, 2, 'user2', 1, '매장상세주소 102호', 'BBQ치킨 2호점', '010-1111-1111', '치킨집입니다', 'OPEN', '11:00', '23:00', 18000,
        '2022-07-16 04:35:47', '2022-07-26 11:30:30'),
       (3, 2, 'user2', 1, '매장상세주소 103호', 'BBQ치킨 3호점', '010-1111-1111', '치킨집입니다', 'OPEN', '11:00', '23:00', 18000,
        '2022-07-16 04:35:47', '2022-07-26 11:30:30'),
       (4, 2, 'user2', 1, '매장상세주소 104호', 'BBQ치킨 4호점', '010-1111-1111', '치킨집입니다', 'CLOSED', '11:00', '23:00', 18000,
        '2022-07-16 04:35:47', '2022-07-26 11:30:30');

CREATE TABLE  IF NOT EXISTS `menu_group`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `store_id`    bigint       NOT NULL,
    `name`        varchar(50)  NOT NULL,
    `info`        varchar(255) NOT NULL,
    `priority`    int          NOT NULL DEFAULT '1',
    `created_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY           `store_id` (`store_id`),
    CONSTRAINT `menu_group_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb3;

INSERT INTO `menu_group`
VALUES (1, 1, '단품', '치킨 단품입니다', 1, '2022-08-02 10:36:47', '2022-08-02 10:36:47'),
       (2, 1, '족발', '족발 단품입니다', 3, '2022-08-02 10:37:06', '2022-08-02 10:37:56'),
       (3, 1, '세트', '치킨 세트입니다', 2, '2022-08-02 10:37:06', '2022-08-02 10:37:56'),
       (4, 1, '사이드메뉴', '사이드메뉴 입니다', 1, '2022-08-02 10:37:06', '2022-08-02 10:37:06'),
       (5, 2, '커피', '커피그룹입니다', 1, '2022-08-11 05:37:42', '2022-08-11 05:37:42'),
       (6, 2, '빵', '빵그룹입니다', 1, '2022-08-11 05:37:42', '2022-08-11 05:37:42'),
       (7, 2, '샐러드', '샐러드그룹입니다', 1, '2022-08-11 05:37:42', '2022-08-11 05:37:42'),
       (8, 2, '주스', '주스그룹입니다', 1, '2022-08-11 05:37:42', '2022-08-11 05:37:42'),
       (9, 2, '커피', '커피그룹입니다', 1, '2022-08-11 05:39:12', '2022-08-11 05:39:12'),
       (10, 2, '빵', '빵그룹입니다', 1, '2022-08-11 05:39:12', '2022-08-11 05:39:12'),
       (11, 2, '샐러드', '샐러드그룹입니다', 1, '2022-08-11 05:39:12', '2022-08-11 05:39:12'),
       (12, 2, '주스', '주스그룹입니다', 1, '2022-08-11 05:39:12', '2022-08-11 05:39:12');

CREATE TABLE IF NOT EXISTS `menu`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT,
    `menu_group_id` bigint       NOT NULL,
    `name`          varchar(50)  NOT NULL,
    `info`          varchar(255) NOT NULL,
    `price`         int          NOT NULL,
    `status`        varchar(20)  NOT NULL DEFAULT 'ONSALE',
    `priority`      int          NOT NULL DEFAULT '0',
    `created_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY             `menu_group_id` (`menu_group_id`),
    CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`menu_group_id`) REFERENCES `menu_group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8mb3;


INSERT INTO `menu`
VALUES (1, 1, '후라이드 치킨', '빵가루', 18000, 'ONSALE', 0, '2022-08-08 09:14:28', '2022-08-08 09:14:28'),
       (2, 1, '양념 치킨', '빨간양념', 20000, 'ONSALE', 0, '2022-08-08 09:14:28', '2022-08-08 09:14:28'),
       (3, 2, '일반족발소', '소자입니다', 28000, 'ONSALE', 0, '2022-08-08 09:14:28', '2022-08-08 09:14:28'),
       (4, 3, '치즈볼', '치즈볼', 5000, 'ONSALE', 0, '2022-08-08 09:14:28', '2022-08-08 09:14:28'),
       (6, 5, '아메리카노', '아메리카노입니다', 3000, 'ONSALE', 1, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (7, 5, '초코라떼', '초코라떼입니다', 4000, 'ONSALE', 2, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (8, 5, '바닐라라떼', '바닐라라떼입니다', 3500, 'ONSALE', 3, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (9, 6, '소보루빵', '소보루빵입니다', 1500, 'ONSALE', 1, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (10, 6, '크림빵', '크림빵입니다', 1500, 'ONSALE', 2, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (11, 6, '피자빵', '피자빵입니다', 3000, 'ONSALE', 3, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (12, 7, '닭가슴살샐러드', '닭가슴살 샐러드 입니다', 12000, 'ONSALE', 2, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (13, 7, '토마토샐러드', '토마토 샐러드 입니다', 10000, 'ONSALE', 1, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (14, 7, '버섯샐러드', '버섯샐러드 입니다', 15000, 'ONSALE', 3, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (15, 8, '수박주스', '수박주스 입니다', 5000, 'ONSALE', 3, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (16, 8, '오렌지주스', '오렌지주스 입니다', 3500, 'ONSALE', 1, '2022-08-11 05:39:12', '2022-08-11 09:31:48'),
       (17, 8, '포토주스', '포도주스 입니다', 4000, 'ONSALE', 2, '2022-08-11 05:39:12', '2022-08-11 09:31:48');


CREATE TABLE  IF NOT EXISTS `options`
(
    `id`      bigint      NOT NULL AUTO_INCREMENT,
    `menu_id` bigint      NOT NULL,
    `name`    varchar(50) NOT NULL,
    `price`   int         NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY       `menu_id` (`menu_id`),
    CONSTRAINT `options_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8mb3;


INSERT INTO `options`
VALUES (3, 1, '치즈볼 추가', 5000),
       (4, 1, '양념소스 추가', 500),
       (5, 1, '갈릭소스 추가', 800),
       (6, 1, '콜라 추가', 1500),
       (7, 2, '치즈볼 추가', 5000),
       (8, 2, '콜라추가', 1500),
       (9, 3, '마늘 추가', 1000),
       (10, 3, '막국수 소 추가', 5000),
       (11, 3, '막국수 중 추가', 7000),
       (12, 3, '막국수 대 추가', 9000),
       (13, 16, '시럽 추가', 0),
       (14, 16, '휘핑크림 추가', 500),
       (15, 16, '아이스크림 추가', 500),
       (16, 16, '사이즈 업', 1000),
       (17, 17, '사이즈 업', 1000),
       (18, 17, '아이스크림 추가', 500),
       (19, 17, '휘핑크림 추가', 500),
       (20, 17, '케이크 추가', 5000);

SET sql_mode = '';

CREATE TABLE  IF NOT EXISTS `orders`
(
    `id`               bigint      NOT NULL AUTO_INCREMENT,
    `user_id`          varchar(50) NOT NULL,
    `store_id`         bigint               DEFAULT NULL,
    `rider_id`         varchar(45)          DEFAULT NULL,
    `order_price`      int         NOT NULL,
    `status`           varchar(40) NOT NULL,
    `start_time`       timestamp NULL DEFAULT '0000-00-00 00:00:00',
    `end_time`         timestamp NULL DEFAULT '0000-00-00 00:00:00',
    `created_at`       timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`      timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `history`          json        NOT NULL,
    `delivery_address` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    KEY                `orders_ibfk_1` (`user_id`),
    KEY                `orders_ibfk_2` (`store_id`),
    CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB AUTO_INCREMENT=5774 DEFAULT CHARSET=utf8mb3;


CREATE TABLE  IF NOT EXISTS `pay`
(
    `id`        bigint      NOT NULL AUTO_INCREMENT,
    `orders_id` bigint      NOT NULL,
    `type`      varchar(40) NOT NULL,
    `status`    varchar(40) NOT NULL,
    PRIMARY KEY (`id`),
    KEY         `orders` (`orders_id`),
    CONSTRAINT `pay_ibfk_1` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=423 DEFAULT CHARSET=utf8mb3;