package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.hw.InMemoryUserDetailsManagerConfiguration;
import ru.otus.hw.models.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import ru.otus.hw.DataGenerator;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.security.AuthorityConstants.ADMIN;
import static ru.otus.hw.security.AuthorityConstants.AUTHOR;
import static ru.otus.hw.security.AuthorityConstants.READER;

@WebMvcTest(BooksController.class)
@Import({GenreConverter.class, AuthorConverter.class, BookConverter.class, SecurityConfig.class, InMemoryUserDetailsManagerConfiguration.class})
public class BookControllerAccessTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private BookConverter bookConverter;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        var dbAuthors = DataGenerator.getDbAuthors();
        var dbGenres = DataGenerator.getDbGenres();
        dbBooks = DataGenerator.getDbBooks(dbAuthors, dbGenres);
    }

      @Test
    @WithMockUser(username = "reader", roles = {READER})
    void postForReaderFailWith403() throws Exception {
        mvc.perform(post("/api/v1/books"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "reader", roles = {READER})
    void putForReaderFailWith403() throws Exception {
        mvc.perform(post("/api/v1/books/{id}", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "reader", roles = {READER})
    void deleteForReaderFailWith403() throws Exception {
        mvc.perform(delete("/api/v1/books/{id}", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "author", roles = {AUTHOR})
    void putForAuthorIsOk() throws Exception {
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
                .andExpect(status().isOk());
    }

    @ParameterizedTest()
    @MethodSource("getUsers")
    void shouldReturnOkForAthenticated(UserInfo user) throws Exception {
        when(bookService.findAll()).thenReturn(dbBooks);
        mvc.perform(get("/api/v1/books")
                        .with(httpBasic(user.getUsername(), user.getPassword())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private static List<UserInfo> getUsers() {
        var admin = new UserInfo("admin", "123", List.of(ADMIN));
        var author = new UserInfo("author", "123", List.of(AUTHOR));
        var reader = new UserInfo("reader", "123", List.of(READER));
        return List.of(admin, author, reader);
    }
}
