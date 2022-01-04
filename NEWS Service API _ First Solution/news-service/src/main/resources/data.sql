INSERT INTO role(role_id,name) VALUES(1,'ROLE_USER');
INSERT INTO role(role_id,name) VALUES(2,'ROLE_PUBLISHER');
INSERT INTO role(role_id,name) VALUES(3,'ROLE_ADMIN');
INSERT INTO user(user_id,password, username) VALUES(700,'$2a$10$hwtrWB5zZtREenI78estWu4Ze4tIKdGUNlH30bb7Fm.TT91iXqcM6','admin');
insert into user_role VALUES(700,3);