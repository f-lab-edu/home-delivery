CREATE TABLE USER(
                     id VARCHAR(50) NOT NULL,
                     email VARCHAR(50) NOT NULL,
                     password VARCHAR(50) NOT NULL,
                     name VARCHAR(10) NOT NULL,
                     phone_number VARCHAR(20) NOT NULL,
                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);

CREATE TABLE RIDER(
                      id VARCHAR(50),
                      email VARCHAR(50) NOT NULL,
                      password VARCHAR(50) NOT NULL,
                      name VARCHAR(10) NOT NULL,
                      phone_number VARCHAR(20) NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (id)
);

CREATE TABLE OWNER(
                      id VARCHAR(50),
                      email VARCHAR(50) NOT NULL,
                      password VARCHAR(50) NOT NULL,
                      name VARCHAR(10) NOT NULL,
                      phone_number VARCHAR(20) NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY (id)
);

CREATE TABLE CATEGORY(
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(20) NOT NULL UNIQUE,
                         PRIMARY KEY(id)
);

CREATE TABLE ADDRESS(
                        id bigint AUTO_INCREMENT,
                        town_name varchar(20) not null unique,
                        PRIMARY KEY (id)
);

CREATE TABLE RIDER_ADDRESS(
                              id BIGINT AUTO_INCREMENT,
                              rider_id VARCHAR(50) NOT NULL,
                              address_id BIGINT NOT NULL,
                              detail_address VARCHAR(50) NOT NULL,
                              alias VARCHAR(20) NOT NULL, -- UNIQUE 넣으면안됨 다른사람들도 사용하니까
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              PRIMARY KEY(id),
                              FOREIGN KEY (rider_id) REFERENCES RIDER(id) ON DELETE CASCADE ON UPDATE CASCADE,
                              FOREIGN KEY (address_id) REFERENCES ADDRESS(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE USER_ADDRESS(
                             id BIGINT AUTO_INCREMENT,
                             user_id VARCHAR(50) NOT NULL,
                             address_id BIGINT NOT NULL,
                             detail_address VARCHAR(50) NOT NULL,
                             alias VARCHAR(20) NOT NULL, -- UNIQUE 옵션넣을지
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY(id),
                             FOREIGN KEY (user_id) REFERENCES USER(id) ON DELETE CASCADE ON UPDATE CASCADE,
                             FOREIGN KEY (address_id) REFERENCES ADDRESS(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE STORE(
                      id BIGINT AUTO_INCREMENT,
                      address_id BIGINT NOT NULL,
                      owner_id VARCHAR(50) NOT NULL,
                      category_id BIGINT NOT NULL,
                      detail_address VARCHAR(50) NOT NULL,
                      name VARCHAR(30) NOT NULL,
                      phone_number VARCHAR(20) NOT NULL,
                      info varchar(255) NOT NULL,
                      status int not null default 1,   -- 상태 기본상태가 무엇인지 DEFAULT 냅둬야할듯
                      open_time VARCHAR(50) NOT NULL, --  오픈시간도 처음에 등록시 DEFAULT넣거나 고민 설정해야할듯
                      end_time VARCHAR(50) NOT NULL,
                      min_price bigint NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      PRIMARY KEY(id),
                      FOREIGN KEY (address_id) REFERENCES ADDRESS(id) ON DELETE CASCADE ON UPDATE CASCADE,
                      FOREIGN KEY (owner_id) REFERENCES OWNER(id) ON DELETE CASCADE ON UPDATE CASCADE,
                      FOREIGN KEY (category_id) REFERENCES CATEGORY(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE menu_group(
                           id BIGINT AUTO_INCREMENT,
                           store_id BIGINT NOT NULL,
                           name VARCHAR(50) NOT NULL,
                           info varchar(255) NOT NULL,
                           priority int NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY(id),
                           FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE menu(
                     id BIGINT AUTO_INCREMENT,
                     menu_group_id BIGINT NOT NULL,
                     name VARCHAR(50) NOT NULL,
                     info varchar(255) NOT NULL,
                     price int NOT NULL,
                     status int NOT NULL default 1,
                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     PRIMARY KEY(id),
                     FOREIGN KEY (menu_group_id) REFERENCES menu_group(id) ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE orders(
                       id BIGINT AUTO_INCREMENT,
                       rider_id varchar(50)NOT NULL,
                       user_id varchar(50) not null,
                       order_price int NOT NULL,
                       delivery_price int NOT NULL,
                       pay_status int not null default 1,
                       status int NOT NULL default 1,
                       satrt_time TIMESTAMP, -- 출발 시간은 라이더가 픽업 해야 바뀜
                       end_time TIMESTAMP, -- 도착 시간은 라이더가 배달 완료 눌러야 바뀜
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY(id),
                       FOREIGN KEY (rider_id) REFERENCES rider(id),
                       FOREIGN KEY (user_id) REFERENCES user(id) -- 유저 삭제시 주문까지 삭제하면 라이더가 주문내역 확인 불가
);



CREATE TABLE orders_history(
                               id BIGINT AUTO_INCREMENT,
                               orders_id bigint NOT NULL,
                               menu_list json not null,
                               primary key(id),
                               FOREIGN KEY (orders_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE orders_menu(
                            id BIGINT AUTO_INCREMENT,
                            orders_id bigint NOT NULL,
                            menu_id bigint not null,
                            amount int not null,
                            primary key(id),
                            FOREIGN KEY (orders_id) REFERENCES orders(id) ON DELETE CASCADE,
                            FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE
);

