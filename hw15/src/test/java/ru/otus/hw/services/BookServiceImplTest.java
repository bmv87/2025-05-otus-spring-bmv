package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис для работы с книгами")
@SpringBootTest()
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Создать книгу")
    @Test
    void shouldCreateBook() {
        var expectedTitle = "title_test";
        var expectedAuthorId = 1L;
        Set<Long> expectedGanres = Set.of(1L, 2L);

        Book createdBook = bookService.insert(expectedTitle, expectedAuthorId, expectedGanres);
        var actualBook = bookService.findById(createdBook.getId());
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getAuthor()).isNotNull();
        assertThat(actualBook.getGenres()).isNotEmpty();
        assertThat(actualBook.getGenres().stream().map(Genre::getId))
                .containsAll(expectedGanres);
        assertThat(actualBook.getTitle()).isEqualTo(expectedTitle);
        assertThat(actualBook.getAuthor().getId()).isEqualTo(expectedAuthorId);
    }


    @DisplayName("Обновить книгу")
    @Test
    void shouldUpdateBook() {
        var expectedTitle = "title_test";
        var expectedAuthorId = 1L;
        Set<Long> expectedGanres = Set.of(2L, 4L);

        Book prevBook = bookService.findById(1L);
        Book editedBook = bookService.update(prevBook.getId(), expectedTitle, expectedAuthorId, expectedGanres);
        Book actualBook = bookService.findById(editedBook.getId());
        assertThat(prevBook).usingRecursiveComparison()
                .ignoringFields("comments")
                .isNotEqualTo(actualBook);
        assertThat(actualBook.getAuthor()).isNotNull();
        assertThat(actualBook.getGenres()).isNotEmpty();
        assertThat(actualBook.getGenres().stream().map(Genre::getId)).containsAll(expectedGanres);
        assertThat(actualBook.getTitle()).isEqualTo(expectedTitle);
        assertThat(actualBook.getAuthor().getId()).isEqualTo(expectedAuthorId);
    }

    @DisplayName("Удалить книгу")
    @Test
    void shouldDeleteBook() {
        var book = bookService.findById(1L);
        bookService.deleteById(1L);

        assertThat(book).isNotNull();
        assertThatThrownBy(() -> {
            bookService.findById(1L);
        }).isInstanceOf(EntityNotFoundException.class);
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
