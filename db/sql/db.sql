
create database scalaitaly;
\c scalaitaly;

create table jobs(
	id uuid primary key default gen_random_uuid (),
	company text not null,
	title text not null,
	description text not null,
	externalUrl text not null,
	salaryLo integer,
	salaryHi integer,
	currency text,
	remote boolean,
	location text not null,
	country text
);

insert into jobs(
	company,
	title,
	description,
	externalUrl,
	salaryLo,
	salaryHi,
	currency,
	remote,
	location,
	country
) values (
	'Rock the JVM',
	'Instructor',
	'Scala teacher',
	'rockthejvm.com',
	0,
	99,
	'EUR',
	true,
	'Bucharest',
	'Romania'
);

insert into jobs(
	company,
	title,
	description,
	externalUrl,
	salaryLo,
	salaryHi,
	currency,
	remote,
	location,
	country
) values (
	'Google',
	'Software Engineer',
	'Writing a money printer',
	'google.com',
	100000,
	9999999,
	'USD',
	false,
	'NYC',
	'USA'
);