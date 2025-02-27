create table users
(
    id            int primary key generated by default as identity,
    password      varchar(256) not null,
    email         varchar(256),
    first_name    varchar(256),
    last_name     varchar(256),
    surname       varchar(256),
    date_of_birth date,
    gender        varchar(256),
    balance       double precision,
    phone_number  varchar(256)

);

create table roles
(
    id   int primary key generated by default as identity,
    name varchar(256) not null
);

create table users_and_roles
(
    user_id int references users (id),
    role_id int references roles (id),
    primary key (user_id, role_id)
);

create table history
(
    id                        int primary key generated by default as identity,
    user_id                   int references users (id) on delete set null,
    phone_number_of_recipient varchar(256),
    date                      date,
    amount                    double precision
);
insert into roles(name) values ('USER');
insert into roles(name) values ('ADMIN');