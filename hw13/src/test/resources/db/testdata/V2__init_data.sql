insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(content, book_id)
values ('content 1_1', 1), ('content 1_2', 1), ('content 2_1', 2), ('content 2_2', 2);

insert into users(id, full_name, username, password)
values (1, 'Library Admin', 'admin', '$2a$10$T6zbnzV9yl84EnZog0j.heRstyvG9ArosPF3lEPgzLMX0g5ZIxj7i'),
       (2, 'Reader', 'reader', '$2a$10$T6zbnzV9yl84EnZog0j.heRstyvG9ArosPF3lEPgzLMX0g5ZIxj7i'),
       (3, 'Book author', 'author', '$2a$10$T6zbnzV9yl84EnZog0j.heRstyvG9ArosPF3lEPgzLMX0g5ZIxj7i');

insert into roles(id, name)
values (1, 'ADMIN'), (2, 'READER'), (3, 'AUTHOR');

insert into users_roles(user_id, role_id)
values (1, 1), (2, 2), (3, 3);


INSERT INTO acl_sid (principal, sid) VALUES
       (1, 'admin'),
       (1, 'author'),
       (1, 'reader'),
       (0, 'ROLE_ADMIN'),
       (0, 'ROLE_AUTHOR'),
       (0, 'ROLE_READER');

INSERT INTO acl_class (class) VALUES
       ('ru.otus.hw.models.Comment'),
       ('ru.otus.hw.models.Book');

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
       (1, 1, NULL, 4, 0), -- comment 1 - owner ROLE_ADMIN
       (1, 2, NULL, 4, 0), -- comment 2 - owner ROLE_ADMIN
       (1, 3, NULL, 4, 0), -- comment 3 - owner ROLE_ADMIN
       (1, 4, NULL, 4, 0), -- comment 4 - owner ROLE_ADMIN
       (2, 1, NULL, 4, 0), -- book 1 - owner ROLE_ADMIN
       (2, 2, NULL, 4, 0), -- book 2 - owner ROLE_ADMIN
       (2, 3, NULL, 4, 0); -- book 3 - owner ROLE_ADMIN


INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask,
                              granting, audit_success, audit_failure) VALUES
       (1, 1, 4, 1, 1, 1, 1), -- comment 1 - order 1 - cid ROLE_ADMIN - mask 1 R granted
       (1, 2, 4, 8, 1, 1, 1), -- comment 1 - order 2 - cid ROLE_ADMIN - mask 8 D granted
       (1, 3, 3, 1, 1, 1, 1), -- comment 1 - order 3 - cid reader - mask 1 R granted
       (1, 4, 3, 2, 1, 1, 1), -- comment 1 - order 4 - cid reader - mask 2 W granted
       (1, 5, 3, 8, 1, 1, 1), -- comment 1 - order 5 - cid reader - mask 8 D granted

       (2, 1, 4, 1, 1, 1, 1), -- comment 2 - order 1 - cid ROLE_ADMIN - mask 1 R granted
       (2, 2, 4, 8, 1, 1, 1), -- comment 2 - order 2 - cid ROLE_ADMIN - mask 8 D granted
       (2, 3, 1, 1, 1, 1, 1), -- comment 2 - order 3 - cid admin - mask 1 R granted
       (2, 4, 1, 2, 1, 1, 1), -- comment 2 - order 4 - cid admin - mask 2 W granted
       (2, 5, 1, 8, 1, 1, 1), -- comment 2 - order 5 - cid admin - mask 8 D granted

       (3, 1, 4, 1, 1, 1, 1), -- comment 3 - order 1 - cid ROLE_ADMIN - mask 1 R granted
       (3, 2, 4, 8, 1, 1, 1), -- comment 3 - order 2 - cid ROLE_ADMIN - mask 8 D granted
       (3, 3, 2, 1, 1, 1, 1), -- comment 3 - order 3 - cid author - mask 1 R granted
       (3, 4, 2, 2, 1, 1, 1), -- comment 3 - order 4 - cid author - mask 2 W granted
       (3, 5, 2, 8, 1, 1, 1), -- comment 3 - order 5 - cid author - mask 8 D granted

       (4, 1, 4, 1, 1, 1, 1), -- comment 4 - order 1 - cid ROLE_ADMIN - mask 1 R granted
       (4, 2, 4, 8, 1, 1, 1), -- comment 4 - order 2 - cid ROLE_ADMIN - mask 8 D granted
       (4, 3, 3, 1, 1, 1, 1), -- comment 4 - order 3 - cid reader - mask 1 R granted
       (4, 4, 3, 2, 1, 1, 1), -- comment 4 - order 4 - cid reader - mask 2 W granted
       (4, 5, 3, 8, 1, 1, 1), -- comment 4 - order 5 - cid reader - mask 8 D granted

       (5, 1, 4, 2, 1, 1, 1), -- book 4 - order 1 - cid ROLE_ADMIN - mask 2 W granted
       (5, 2, 4, 8, 1, 1, 1); -- book 4 - order 2 - cid ROLE_ADMIN - mask 8 D granted