create table countries
(
    id   int primary key,
    name varchar(50) not null unique
);

INSERT INTO countries (id, name)
VALUES (1, 'USA');
INSERT INTO countries (id, name)
VALUES (2, 'France');
INSERT INTO countries (id, name)
VALUES (3, 'Brazil');
INSERT INTO countries (id, name)
VALUES (4, 'Italy');
INSERT INTO countries (id, name)
VALUES (5, 'Canada');