drop table if exists cat_order_line;
drop table if exists cat_order;

CREATE TABLE `cat_order`
(
    id                 varchar(36) NOT NULL,
    created_on       datetime(6)  DEFAULT NULL,
    customer_ref       varchar(255) DEFAULT NULL,
    updated_on datetime(6)  DEFAULT NULL,
    version            int       DEFAULT NULL,
    customer_id        varchar(36)  DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (customer_id) REFERENCES customer (id)
) ENGINE = InnoDB;

CREATE TABLE `cat_order_line`
(
    id                 varchar(36) NOT NULL,
    cat_id            varchar(36) DEFAULT NULL,
    created_on       datetime(6) DEFAULT NULL,
    updated_on datetime(6) DEFAULT NULL,
    order_quantity     int         DEFAULT NULL,
    quantity_allocated int         DEFAULT NULL,
    version            int      DEFAULT NULL,
    cat_order_id      varchar(36) DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (cat_order_id) REFERENCES cat_order (id),
    CONSTRAINT FOREIGN KEY (cat_id) REFERENCES cat (id)
) ENGINE = InnoDB;