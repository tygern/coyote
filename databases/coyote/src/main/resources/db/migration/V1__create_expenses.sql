create table expenses
(
    id            binary(16) not null,
    amount        bigint not null,
    currency_code char(3) not null,
    instant       bigint not null,

    constraint primary key (id)
)
    engine = innodb
    default charset = utf8;
