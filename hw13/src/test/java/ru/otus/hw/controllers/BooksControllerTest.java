package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.DataGenerator;
import ru.otus.hw.InMemoryUserDetailsManagerConfiguration;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookViewDto;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.dto.FieldErrorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.security.AuthorityConstants.ADMIN;

@WebMvcTest(BooksController.class)
@Import({GenreMapper.class, AuthorMapper.class, BookMapper.class, SecurityConfig.class, InMemoryUserDetailsManagerConfiguration.class})
public class BooksControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

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
        dbAuthors = DataGenerator.getDbAuthors();
        dbGenres = DataGenerator.getDbGenres();
        dbBooks = DataGenerator.getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("Можно получить список книг с правильным типом данных и статусом 200.")
    @Test
    @WithMockUser
    void shouldReturnBookDtoArrayWithCorrectContentType() throws Exception {
        List<BookDto> expectedBooks = dbBooks
                .stream()
                .map(bookMapper::toListItemDto)
                .toList();
        when(bookService.findAll()).thenReturn(expectedBooks);

        mvc.perform(get("/api/v1/books").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));
    }

    @DisplayName("Можно получить книгу по идентификатору. В ответе правильный тип данных и статусом 200.")
    @Test
    @WithMockUser
    void shouldReturnBookDtoWithCorrectContentType() throws Exception {
        var book = dbBooks.get(0);
        BookViewDto expectedBook = bookMapper.toBookViewDto(book);
        when(bookService.findById(1L)).thenReturn(expectedBook);

        mvc.perform(get("/api/v1/books/{id}", String.valueOf(book.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @DisplayName("Если книга не найдена то получаем правильный ответ и статус 404.")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void shouldReturnErrorDtoWithCorrectContentTypeAndStatus() throws Exception {
        var errorMessage = "Not found";
        var expectedErrorDto = new ErrorDto("404", errorMessage, null);
        when(bookService.findById(1L)).thenThrow(new EntityNotFoundException(errorMessage));
        mvc.perform(get("/api/v1/books/{id}", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expectedErrorDto)));
    }

    @DisplayName("Можно обновить книгу и получить правильный ответ и статус 200.")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void shouldUpdateBookAndReturnBookDtoWithCorrectContentType() throws Exception {
        var book = dbBooks.get(0);
        var bookEditDto = bookMapper.toUpdateDto(book);
        var bookViewDto = bookMapper.toBookViewDto(book);
        when(bookService.update(
                anyLong(),
                any())).thenReturn(bookViewDto);

        mvc.perform(put("/api/v1/books/{id}", String.valueOf(book.getId()))
                        .content(mapper.writeValueAsString(bookEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(bookViewDto)));
    }

    @DisplayName("При обновлении книги с неправильным набором параметров получаем ожидаемый ответ и статус 400.")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void shouldReturnErrorDtoWithCorrectStatusForIncorrectUpdateBookDto() throws Exception {
        var book = dbBooks.get(0);
        var incorrectBook = bookMapper.toUpdateDto(dbBooks.get(0));
        incorrectBook.setTitle(null);
        var expectedErrorDto = new ErrorDto(
                "400",
                "Validation error",
                List.of(new FieldErrorDto("title", "must not be blank")));

        MvcResult mvcResult = mvc.perform(put("/api/v1/books/{id}",
                        String.valueOf(book.getId()))
                        .content(mapper.writeValueAsString(incorrectBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        var actualErrorDto = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), ErrorDto.class);

        assertEquals(expectedErrorDto, actualErrorDto);
        assertThat(actualErrorDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedErrorDto);
    }

    @DisplayName("Можно обновить книгу и получить правильный ответ и статус 200.")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void shouldCreateBookAndReturnBookDtoWithCorrectContentType() throws Exception {
        var book = dbBooks.get(0);
        var createBookDto = bookMapper.toUpdateDto(book);
        var bookViewDto = bookMapper.toBookViewDto(book);
        when(bookService.insert(any())).thenReturn(bookViewDto);

        mvc.perform(post("/api/v1/books", String.valueOf(book.getId()))
                        .content(mapper.writeValueAsString(createBookDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(bookViewDto)));
    }

    @DisplayName("Можно удалить книгу и получить статус 200.")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mvc.perform(delete("/api/v1/books/{id}", '1')
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(bookService, times(1)).deleteById(1L);
    }
}
