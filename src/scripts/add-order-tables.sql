drop table if exists cat_order cascade;
drop table if exists cat_order_line cascade;

create table cat_order
(
    version      integer,
    created_on   timestamp(6),
    updated_on   timestamp(6),
    customer_id  varchar(36),
    id           varchar(36) not null,
    customer_ref varchar(255),
    primary key (id),
    CONSTRAINT FOREIGN KEY (customer_id) REFERENCES customer (id)
);
create table cat_order_line
(
    order_quantity     integer,
    quantity_allocated integer,
    version            integer,
    created_on         timestamp(6),
    updated_on         timestamp(6),
    id                 varchar(36) not null,
    primary key (id),
    CONSTRAINT FOREIGN KEY (cat_order_id) REFERENCES cat_order (id),
    CONSTRAINT FOREIGN KEY (cat_id) REFERENCES cat (id)
);