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
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookViewDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/books"})
public class BooksController {

    private final BookService bookService;

    @GetMapping()
    public List<BookDto> findAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookViewDto findBookById(@PathVariable("id") long id) {
        return bookService.findById(id);
    }

    @PostMapping()
    public BookViewDto insertBook(@Valid @RequestBody BookCreateDto newBook) {
        return bookService.insert(newBook);
    }

    @PutMapping("/{id}")
    public BookViewDto updateBook(@PathVariable("id") long id,
                                  @Valid @RequestBody BookCreateDto editedBook) {
        return bookService.update(id, editedBook);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
