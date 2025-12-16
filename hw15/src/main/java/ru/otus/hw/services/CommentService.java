package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {
    Comment findById(long id);

    List<Comment> findByBookId(long bookId);

    Comment insert(long bookId, String content);

    Comment update(long id, String content);

    void deleteById(long id);
}
