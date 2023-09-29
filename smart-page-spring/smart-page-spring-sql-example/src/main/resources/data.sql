create table item
(
    id   bigint primary key,
    name varchar(30) unique
);

insert into item(id, name)
values (1, 'T-SHIRT'),
       (2, 'SHOES'),
       (3, 'CAP');

create table store
(
    id   bigint primary key,
    name varchar(30) unique
);

insert into store(id, name)
values (1, 'PARIS'),
       (2, 'SEOUL'),
       (3, 'BEIJING');

create table sale
(
    id       bigint primary key,
    item_id  bigint not null,
    store_id bigint not null
);

insert into sale(id, item_id, store_id)
values (1, 1, 1),
       (2, 1, 2),
       (3, 1, 3),
       (4, 1, 1),
       (5, 3, 2);