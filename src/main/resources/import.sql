insert into nodes (inode, name, type, parent, content) values (100, null, 4, null, null);
insert into nodes (inode, name, type, parent, content) values (101, 'outer.txt', 8, 100, e'outer text\n');
insert into nodes (inode, name, type, parent, content) values (102, 'dir', 4, 100, null);
insert into nodes (inode, name, type, parent, content) values (103, 'inner1.txt', 8, 102, e'inner1 text\n');
insert into nodes (inode, name, type, parent, content) values (104, 'inner2.txt', 8, 102, e'inner2 text\n');

insert into tokens values ('admin');