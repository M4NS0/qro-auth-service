insert into role(name)
select 'ROLE_ADMIN' where not exists (select 1 from role where name = 'ROLE_ADMIN');

insert into role(name)
select 'ROLE_USER' where not exists (select 1 from role where name = 'ROLE_USER');
