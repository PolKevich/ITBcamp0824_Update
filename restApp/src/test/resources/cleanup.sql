--
DELETE FROM user_project;
DELETE FROM project;
DELETE FROM user;

--
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE project AUTO_INCREMENT = 1;
ALTER TABLE user_project AUTO_INCREMENT = 1;