create schema global;
create table global.PersData (id NUMERIC(10,0), name VARCHAR(50), surname VARCHAR(50), sexType VARCHAR(10));

insert into global.PersData (id, name, surname, sexType) values (1, 'John', 'Foo', 'M');
insert into global.PersData (id, name, surname, sexType) values (2, 'Mice', 'Bike', 'M');
insert into global.PersData (id, name, surname, sexType) values (3, 'Nicole', 'Brown', 'F');
insert into global.PersData (id, name, surname, sexType) values (4, 'Susan', 'Graves', 'F');
insert into global.PersData (id, name, surname, sexType) values (5, 'Dan', 'Lovejoy', 'M');