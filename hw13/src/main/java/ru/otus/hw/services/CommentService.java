package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentViewDto;

import java.util.List;

public interface CommentService {

    CommentViewDto findById(long id);

    List<CommentViewDto> findByBookId(long bookId);

    CommentViewDto insert(CommentDto commentDto);

    CommentViewDto update(long id, CommentDto commentDto);

    void deleteById(long id);
}
