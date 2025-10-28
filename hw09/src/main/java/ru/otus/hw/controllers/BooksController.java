package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    private final AuthorConverter authorConverter;

    private final BookConverter bookConverter;

    @GetMapping(value = {"/books/", "/books"})
    public String findAllBooks(Model model) {
        var books = bookService.findAll().stream()
                .map(bookConverter::toListItemDto)
                .toList();
        model.addAttribute("books", books);
        return "books/list";
    }

    @GetMapping("/books/view/{id}")
    public String findBookById(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id);
        var bookDto = bookConverter.toBookWithCommentsDto(book);
        model.addAttribute("book", bookDto);
        return "books/view";
    }

    @GetMapping("/books/create")
    public String insertBook(Model model) {
        model.addAttribute("book", new Book());
        addDictionariesToModel(model);
        return "books/create";
    }

    @PostMapping("/books/create")
    public String insertBook(@Valid @ModelAttribute("book") BookDto newBook,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasFieldErrors()) {
            addDictionariesToModel(model);
            return "books/create";
        }
        bookService.insert(newBook.getTitle(), newBook.getAuthor(), newBook.getGenres());
        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String updateBook(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id);
        var bookDto = bookConverter.toEditDto(book);

        model.addAttribute("book", bookDto);
        addDictionariesToModel(model);
        return "books/edit";
    }

    @PutMapping("/books/edit")
    public String updateBook(@Valid @ModelAttribute("book") BookEditDto editedBook,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasFieldErrors()) {
            addDictionariesToModel(model);
            return "books/edit";
        }
        bookService.update(
                editedBook.getId(),
                editedBook.getTitle(),
                editedBook.getAuthor(),
                editedBook.getGenres());
        return "redirect:/books";
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/books/";
    }

    private void addDictionariesToModel(Model model) {
        var genres = genreService.findAll()
                .stream()
                .map(genreConverter::toViewDto)
                .toList();
        var authors = authorService.findAll()
                .stream()
                .map(authorConverter::toViewDto)
                .toList();
        model.addAttribute("genres", genres);
        model.addAttribute("authors", authors);
    }
}
