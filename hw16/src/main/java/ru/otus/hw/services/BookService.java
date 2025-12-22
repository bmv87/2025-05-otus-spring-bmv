package ru.otus.hw.services;

import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookViewDto;

import java.util.List;

public interface BookService {

    BookViewDto findById(long id);

    List<BookDto> findAll();

    BookViewDto insert(BookCreateDto bookDto);

    BookViewDto update(long id, BookCreateDto bookDto);

    void deleteById(long id);
}
