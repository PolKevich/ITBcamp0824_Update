--liquibase formatted sql

--changeset PolKevich:1
create table if not exists project(
    project_id   bigint primary key auto_increment,
    projectname varchar(60) null,
    projectdescription varchar(150) null
);
--rollback DROP TABLE project

--changeset PolKevich:2
create table if not exists user(
    user_id bigint primary key auto_increment,
    lastname  varchar(40) null,
    firstname  varchar(20) null,
    patronymic varchar(40) null,
    email   varchar(50) null,
    post   varchar(40) null
);
--rollback DROP TABLE user

--changeset PolKevich:3
create table if not exists user_project (
    user_id bigint not null,
    project_id bigint not null,
    primary key (user_id, project_id),
    foreign key (user_id) references user (user_id),
    foreign key (project_id) references project (project_id)
);
--rollback DROP TABLE user_project