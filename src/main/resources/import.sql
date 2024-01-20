insert into nodes (inode, name, type, parent) values (100, null, 4, null);
insert into nodes (inode, name, type, parent) values (101, 'outer.txt', 8, 100);
insert into nodes (inode, name, type, parent) values (102, 'dir', 4, 100);
insert into nodes (inode, name, type, parent) values (103, 'inner1.txt', 8, 102);
insert into nodes (inode, name, type, parent) values (104, 'inner2.txt', 8, 102);

insert into tokens values ('admin');