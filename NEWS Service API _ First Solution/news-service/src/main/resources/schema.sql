create table news (news_id bigint generated by default as identity, creation_date timestamp not null, is_published boolean, text varchar(255), title varchar(255), valid_from timestamp, valid_to timestamp, primary key (news_id))
create table picture (picture_id bigint not null, picture_data varchar(255), picture_name varchar(255), news_id bigint, primary key (picture_id))
create table read_status (read_status_id bigint not null, read_date timestamp not null, news_id bigint, user_id integer, primary key (read_status_id))
create table role (role_id integer not null, name varchar(255), primary key (role_id))
create table user (user_id integer not null, password varchar(255), username varchar(255), primary key (user_id))
create table user_role (user_id integer not null, role_id integer not null, primary key (user_id, role_id))
create sequence hibernate_sequence start with 1 increment by 1
alter table picture add constraint FKgy7icmlqh1ib0fecba0p4d0p1 foreign key (news_id) references news
alter table read_status add constraint FK6drim8gvlcry7fcsbgkvt1dcf foreign key (news_id) references news
alter table read_status add constraint FKos41ps6a5qkacbphh5rij2tud foreign key (user_id) references user
alter table user_role add constraint FKa68196081fvovjhkek5m97n3y foreign key (role_id) references role
alter table user_role add constraint FK859n2jvi8ivhui0rl0esws6o foreign key (user_id) references user