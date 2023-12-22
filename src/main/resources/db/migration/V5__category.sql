drop table if exists cat_category;
drop table if exists category;

create table category
(
    id                 varchar(36) NOT NULL PRIMARY KEY,
    description        varchar(50),
    created_on       timestamp(6),
    updated_on timestamp(6) DEFAULT NULL,
    version            int      DEFAULT NULL
) ENGINE = InnoDB;

create table cat_category
(
    cat_id     varchar(36) NOT NULL,
    category_id varchar(36) NOT NULL,
    primary key (cat_id, category_id),
    constraint pc_cat_id_fk FOREIGN KEY (cat_id) references cat (id),
    constraint pc_category_id_fk FOREIGN KEY (category_id) references category (id)
) ENGINE = InnoDB;