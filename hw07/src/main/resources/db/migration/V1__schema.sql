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