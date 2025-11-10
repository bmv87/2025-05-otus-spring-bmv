create table users (
    id bigserial NOT NULL,
    full_name varchar(255) NOT NULL,
    username varchar(30) NOT NULL,
    password varchar(255) NOT NULL,
    primary key (id)
);

create table roles (
    id bigserial NOT NULL,
    name varchar(255) NOT NULL,
    primary key (id)
);

create table users_roles (
    user_id bigint references users(id) NOT NULL,
    role_id bigint references roles(id) NOT NULL,
    primary key (user_id, role_id)
);

create table authors (
    id bigserial NOT NULL,
    full_name varchar(255) NOT NULL,
    primary key (id)
);

create table genres (
    id bigserial NOT NULL,
    name varchar(255) NOT NULL,
    primary key (id)
);

create table books (
    id bigserial NOT NULL,
    title varchar(255) NOT NULL,
    author_id bigint references authors (id) on delete cascade NOT NULL,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade NOT NULL,
    genre_id bigint references genres(id) on delete cascade NOT NULL,
    primary key (book_id, genre_id)
);

create table comments (
    id bigserial NOT NULL,
    content varchar(255) NOT NULL,
    book_id bigint references books(id) on delete cascade NOT NULL,
    primary key (id)
);