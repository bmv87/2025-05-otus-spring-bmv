package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.DataGenerator;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookViewDto;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.dto.FieldErrorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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


@WebMvcTest(BooksController.class)
@Import({GenreConverter.class, AuthorConverter.class, BookConverter.class, SecurityConfig.class})
public class BooksControllerTest {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
            UserDetails user = User
                    .builder()
                    .username("admin")
                    .password("$2a$10$T6zbnzV9yl84EnZog0j.heRstyvG9ArosPF3lEPgzLMX0g5ZIxj7i")
                    .roles("ADMIN")
                    .build();
            return new InMemoryUserDetailsManager(user);
        }
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

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
        dbAuthors = DataGenerator.getDbAuthors();
        dbGenres = DataGenerator.getDbGenres();
        dbBooks = DataGenerator.getDbBooks(dbAuthors, dbGenres);
    }

    @Test
    @WithMockUser
    void shouldReturnBookDtoArrayWithCorrectContentType() throws Exception {
        when(bookService.findAll()).thenReturn(dbBooks);
        List<BookDto> expectedBooks = dbBooks
                .stream()
                .map(bookConverter::toListItemDto)
                .toList();
        mvc.perform(get("/api/v1/books").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));
    }

    @Test
    @WithMockUser
    void shouldReturnBookDtoWithCorrectContentType() throws Exception {
        var book = dbBooks.get(0);
        when(bookService.findById(1L)).thenReturn(book);
        BookViewDto expectedBook = bookConverter.toBookViewDto(book);
        mvc.perform(get("/api/v1/books/{id}", String.valueOf(book.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnErrorDtoWithCorrectContentTypeAndStatus() throws Exception {
        var errorMessage = "Not found";
        var expectedErrorDto = new ErrorDto("404", errorMessage, null);
        when(bookService.findById(1L)).thenThrow(new EntityNotFoundException(errorMessage));
        mvc.perform(get("/api/v1/books/{id}", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expectedErrorDto)));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateBookAndAndReturnBookDtoWithCorrectContentType() throws Exception {
        var book = dbBooks.get(0);
        var bookEditDto = bookConverter.toUpdateDto(dbBooks.get(0));
        when(bookService.update(
                anyLong(),
                anyString(),
                anyLong(),
                any())).thenReturn(book);

        mvc.perform(put("/api/v1/books/{id}", String.valueOf(bookEditDto.getId()))
                        .content(mapper.writeValueAsString(bookEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(bookEditDto)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnErrorDtoWithCorrectStatusForIncorrectUpdateBookDto() throws Exception {
        var book = dbBooks.get(0);
        var incorrectBook = bookConverter.toUpdateDto(dbBooks.get(0));
        incorrectBook.setTitle(null);
        var expectedErrorDto = new ErrorDto(
                "400",
                "Validation error",
                List.of(new FieldErrorDto("title", "must not be blank")));

        when(bookService.update(
                anyLong(),
                anyString(),
                anyLong(),
                any())).thenReturn(book);

        MvcResult mvcResult = mvc.perform(put("/api/v1/books/{id}",
                        String.valueOf(incorrectBook.getId()))
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

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateBookAndReturnBookDtoWithCorrectContentType() throws Exception {
        var book = dbBooks.get(0);
        var bookEditDto = bookConverter.toUpdateDto(dbBooks.get(0));
        when(bookService.insert(anyString(), anyLong(), any())).thenReturn(book);
        var genres = book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        BookCreateDto expectedBook = new BookCreateDto(book.getTitle(), book.getAuthor().getId(), genres);

        mvc.perform(post("/api/v1/books", String.valueOf(book.getId()))
                        .content(mapper.writeValueAsString(expectedBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(bookEditDto)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldRedirectAfterDelete() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mvc.perform(delete("/api/v1/books/{id}", '1')
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(bookService, times(1)).deleteById(1L);
    }
}
