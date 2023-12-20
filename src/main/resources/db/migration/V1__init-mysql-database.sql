
    drop table if exists cat;

    create table cat (
        age integer not null,
        version integer,
        created_on datetime(6),
        updated_on datetime(6),
        id varchar(36) not null,
        name varchar(50) not null,
        primary key (id)
    ) engine=InnoDB;