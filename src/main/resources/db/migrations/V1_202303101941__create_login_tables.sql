CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(30)  NOT NULL,
    email    VARCHAR(45)  NOT NULL,
    password VARCHAR(150) NOT NULL,
    CONSTRAINT name_unique UNIQUE (name),
    CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS role
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(45)
);

insert into role(name)
select 'ROLE_ADMIN' where not exists (select 1 from role where name = 'ROLE_ADMIN');

insert into role(name)
select 'ROLE_USER' where not exists (select 1 from role where name = 'ROLE_USER');


CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id),
    CONSTRAINT user_role_user_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT user_role_role_fk FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);



