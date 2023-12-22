drop table if exists customer cascade;
create table customer
(
    version    integer,
    created_on timestamp(6),
    updated_on timestamp(6),
    id         varchar(36) not null,
    name       varchar(50) not null,
    email      varchar(255),
    primary key (id)
);