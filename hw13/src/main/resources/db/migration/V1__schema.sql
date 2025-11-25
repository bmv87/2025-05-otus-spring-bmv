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

CREATE TABLE IF NOT EXISTS acl_sid (
  id bigserial NOT NULL PRIMARY KEY,
  principal tinyint NOT NULL,
  sid varchar(100) NOT NULL
);

ALTER TABLE acl_sid ADD CONSTRAINT unique_uk_1 UNIQUE(sid,principal);

CREATE TABLE IF NOT EXISTS acl_class (
  id bigserial NOT NULL PRIMARY KEY,
  class varchar(255) NOT NULL
);

ALTER TABLE acl_class ADD CONSTRAINT unique_uk_2 UNIQUE(class);

CREATE TABLE IF NOT EXISTS acl_entry (
  id bigserial NOT NULL PRIMARY KEY,
  acl_object_identity bigint NOT NULL,
  ace_order int NOT NULL,
  sid bigint NOT NULL,
  mask int NOT NULL,
  granting tinyint NOT NULL,
  audit_success tinyint NOT NULL,
  audit_failure tinyint NOT NULL
);

ALTER TABLE acl_entry ADD CONSTRAINT unique_uk_4 UNIQUE(acl_object_identity,ace_order);

CREATE TABLE IF NOT EXISTS acl_object_identity (
  id bigserial NOT NULL PRIMARY KEY,
  object_id_class bigint NOT NULL,
  object_id_identity bigint NOT NULL,
  parent_object bigint DEFAULT NULL,
  owner_sid bigint DEFAULT NULL,
  entries_inheriting tinyint NOT NULL
);

ALTER TABLE acl_object_identity ADD CONSTRAINT unique_uk_3 UNIQUE(object_id_class,object_id_identity);

ALTER TABLE acl_entry
ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);