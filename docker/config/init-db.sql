create schema global;
create table global.PersData (id NUMERIC(10,0), name VARCHAR(50), surname VARCHAR(50), sexType VARCHAR(10));
create table global.AddrData (id NUMERIC(10,0), strt VARCHAR(50), strtNumber VARCHAR(50), town VARCHAR(50), persId NUMERIC(10,0));

insert into global.PersData (id, name, surname, sexType) values (1, 'John', 'Foo', 'M');
insert into global.PersData (id, name, surname, sexType) values (2, 'Mice', 'Bike', 'M');
insert into global.PersData (id, name, surname, sexType) values (3, 'Nicole', 'Brown', 'F');
insert into global.PersData (id, name, surname, sexType) values (4, 'Susan', 'Graves', 'F');
insert into global.PersData (id, name, surname, sexType) values (5, 'Dan', 'Lovejoy', 'M');

insert into global.AddrData (id, strt, strtNumber, town, persId) values (10, 'Wisteria Lane', '20', 'New York', 1);
insert into global.AddrData (id, strt, strtNumber, town, persId) values (20, 'Frederic Street', '1A', 'Boston', 2);
insert into global.AddrData (id, strt, strtNumber, town, persId) values (30, 'Woodbery St', '432', 'Washington', 3);
insert into global.AddrData (id, strt, strtNumber, town, persId) values (40, 'Ager Rd', '100', 'Phoenix', 4);
insert into global.AddrData (id, strt, strtNumber, town, persId) values (50, 'Franklin St', '67', 'Denver', 5);