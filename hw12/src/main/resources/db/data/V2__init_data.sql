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