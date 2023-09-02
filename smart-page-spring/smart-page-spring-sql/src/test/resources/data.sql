create table person
(
    id         bigint      not null primary key,
    first_name varchar(30) not null,
    role varchar(50) not null
);

insert into person(id, first_name, role)
values (1, 'Arthur', 'Roi du royaume de Logres'),
       (2, 'Leodagan', 'Roi de Carmélide'),
       (3, 'Perceval', 'Venu des etoiles'),
       (4, 'Karadoc', 'Le croque monsieur'),
       (5, 'Kadoc', 'Elle est où la poulette ?');