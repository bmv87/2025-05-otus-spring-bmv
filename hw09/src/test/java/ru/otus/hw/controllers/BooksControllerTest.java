package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorViewDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.dto.BookListItemDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.dto.GenreViewDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BooksController.class)
@Import({GenreConverter.class, AuthorConverter.class, BookConverter.class, CommentConverter.class})
public class BooksControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private AuthorConverter authorConverter;

    @Autowired
    private GenreConverter genreConverter;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private AuthorService authorService;


    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        var book = dbBooks.get(0);
        var comments = IntStream.range(1, 4).boxed()
                .map(id -> new Comment(id,
                        "Comment in test" + id,
                        book
                ))
                .toList();
        book.setComments(comments);
    }

    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findAll()).thenReturn(dbBooks);
        List<BookListItemDto> expectedBooks = dbBooks
                .stream()
                .map(bookConverter::toListItemDto)
                .toList();
        mvc.perform(get("/books"))
                .andExpect(view().name("books/list"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @Test
    void shouldRenderViewPageWithCorrectViewAndModelAttributes() throws Exception {
        var book = dbBooks.get(0);
        when(bookService.findById(1L)).thenReturn(book);
        BookWithCommentsDto expectedBook = bookConverter.toBookWithCommentsDto(book);
        mvc.perform(get("/books/view/{id}", String.valueOf(book.getId())))
                .andExpect(view().name("books/view"))
                .andExpect(model().attribute("book", expectedBook));
    }

    @Test
    void shouldRenderErrorPageWithCorrectViewAndModelAttributes() throws Exception {
        var errorMessage = "Not found";
        var expectedErrorDto = new ErrorDto("404", errorMessage, null);
        when(bookService.findById(1L)).thenThrow(new EntityNotFoundException(errorMessage));
        mvc.perform(get("/books/view/{id}", "1"))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("error", expectedErrorDto));
    }

    @Test
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        var book = dbBooks.get(0);
        when(bookService.findById(1L)).thenReturn(book);
        when(genreService.findAll()).thenReturn(dbGenres);
        when(authorService.findAll()).thenReturn(dbAuthors);
        BookEditDto expectedBook = bookConverter.toEditDto(book);
        List<GenreViewDto> expectedGenres = dbGenres.stream()
                .map(genreConverter::toViewDto)
                .toList();
        List<AuthorViewDto> expectedAuthors = dbAuthors.stream()
                .map(authorConverter::toViewDto)
                .toList();
        mvc.perform(get("/books/edit/{id}", String.valueOf(book.getId())))
                .andExpect(view().name("books/edit"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("genres", expectedGenres))
                .andExpect(model().attribute("authors", expectedAuthors));
    }

    @Test
    void shouldUpdateBookAndRedirect() throws Exception {
        var book = dbBooks.get(0);
        var bookEditDto = bookConverter.toEditDto(dbBooks.get(0));
        when(bookService.update(
                bookEditDto.getId(),
                bookEditDto.getTitle(),
                bookEditDto.getAuthor(),
                bookEditDto.getGenres())).thenReturn(book);

        mvc.perform(put("/books/edit")
                        .param("id", String.valueOf(bookEditDto.getId()))
                        .param("title", bookEditDto.getTitle())
                        .param("author", String.valueOf(bookEditDto.getAuthor()))
                        .param("genres", String.valueOf(bookEditDto.getGenres().stream().findAny().get()))
                )
                .andExpect(view().name("redirect:/books"));
    }

    @Test
    void shouldRenderRenderEditPageWithPageWithCorrectViewAndModelAttributes() throws Exception {
        var book = dbBooks.get(0);
        when(bookService.findById(1L)).thenReturn(book);
        when(genreService.findAll()).thenReturn(dbGenres);
        when(authorService.findAll()).thenReturn(dbAuthors);
        BookEditDto bookEditDto = bookConverter.toEditDto(book);
        var genres = bookEditDto.getGenres().toArray();
        List<GenreViewDto> expectedGenres = dbGenres.stream()
                .map(genreConverter::toViewDto)
                .toList();
        List<AuthorViewDto> expectedAuthors = dbAuthors.stream()
                .map(authorConverter::toViewDto)
                .toList();
        mvc.perform(put("/books/edit")
                        .param("id", String.valueOf(bookEditDto.getId()))
                        .param("title", bookEditDto.getTitle())
                        .param("genres", String.valueOf(genres[0]))
                        .param("genres", String.valueOf(genres[1]))
                )
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeHasErrors("book"))
                .andExpect(model().attribute("genres", expectedGenres))
                .andExpect(model().attribute("authors", expectedAuthors));
        mvc.perform(put("/books/edit")
                        .param("id", String.valueOf(bookEditDto.getId()))
                        .param("author", String.valueOf(bookEditDto.getAuthor()))
                        .param("genres", String.valueOf(genres[0]))
                        .param("genres", String.valueOf(genres[1]))
                )
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeHasErrors("book"))
                .andExpect(model().attribute("genres", expectedGenres))
                .andExpect(model().attribute("authors", expectedAuthors));
        mvc.perform(put("/books/edit")
                        .param("id", String.valueOf(bookEditDto.getId()))
                        .param("title", bookEditDto.getTitle())
                        .param("author", String.valueOf(bookEditDto.getAuthor()))
                )
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeHasErrors("book"))
                .andExpect(model().attribute("genres", expectedGenres))
                .andExpect(model().attribute("authors", expectedAuthors));
        mvc.perform(put("/books/edit")
                        .param("title", bookEditDto.getTitle())
                        .param("author", String.valueOf(bookEditDto.getAuthor()))
                        .param("genres", String.valueOf(genres[0]))
                )
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeHasErrors("book"))
                .andExpect(model().attribute("genres", expectedGenres))
                .andExpect(model().attribute("authors", expectedAuthors));
    }

    @Test
    void shouldRenderCreatePageWithCorrectViewAndModelAttributes() throws Exception {
        var book = dbBooks.get(0);
        when(bookService.findById(1L)).thenReturn(book);
        when(genreService.findAll()).thenReturn(dbGenres);
        when(authorService.findAll()).thenReturn(dbAuthors);
        BookDto expectedBook = new BookDto();
        List<GenreViewDto> expectedGenres = dbGenres.stream()
                .map(genreConverter::toViewDto)
                .toList();
        List<AuthorViewDto> expectedAuthors = dbAuthors.stream()
                .map(authorConverter::toViewDto)
                .toList();
        mvc.perform(get("/books/create", String.valueOf(book.getId())))
                .andExpect(view().name("books/create"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("genres", expectedGenres))
                .andExpect(model().attribute("authors", expectedAuthors));
    }


    @Test
    void shouldRedirectAfterDelete() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mvc.perform(delete("/books/{id}", '1'))
                .andExpect(view().name("redirect:/books"));

        verify(bookService, times(1)).deleteById(1L);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
