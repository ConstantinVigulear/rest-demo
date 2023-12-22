drop table if exists cat_order_shipment;

CREATE TABLE cat_order_shipment
(
    id                 VARCHAR(36) NOT NULL PRIMARY KEY,
    cat_order_id            VARCHAR(36) UNIQUE,
    tracking_number    VARCHAR(50),
    created_on       TIMESTAMP,
    updated_on DATETIME(6) DEFAULT NULL,
    version            INT      DEFAULT NULL,
    CONSTRAINT cos_pk FOREIGN KEY (cat_order_id) REFERENCES cat_order (id)
) ENGINE = InnoDB;

ALTER TABLE cat_order
    ADD COLUMN cat_order_shipment_id VARCHAR(36);

ALTER TABLE cat_order
    ADD CONSTRAINT cos_shipment_fk
        FOREIGN KEY (cat_order_shipment_id) REFERENCES cat_order_shipment (id);