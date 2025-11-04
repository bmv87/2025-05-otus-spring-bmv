package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.BookViewDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/books"})
public class BooksController {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @GetMapping()
    public List<BookDto> findAllBooks() {
        var books = bookService.findAll().stream()
                .map(bookConverter::toListItemDto)
                .toList();
        return books;
    }

    @GetMapping("/{id}")
    public BookViewDto findBookById(@PathVariable("id") long id) {
        var book = bookService.findById(id);
        var bookDto = bookConverter.toBookViewDto(book);
        return bookDto;
    }

    @PostMapping()
    public BookUpdateDto insertBook(@Valid @RequestBody BookCreateDto newBook) {
        var book = bookService.insert(newBook.getTitle(), newBook.getAuthor(), newBook.getGenres());
        return bookConverter.toUpdateDto(book);
    }

    @PutMapping("/{id}")
    public BookUpdateDto updateBook(@PathVariable("id") long id,
                                    @Valid @RequestBody BookCreateDto editedBook) {
        var book = bookService.update(
                id,
                editedBook.getTitle(),
                editedBook.getAuthor(),
                editedBook.getGenres());
        var bookDto = bookConverter.toUpdateDto(book);
        return bookDto;
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }

}
