package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.StringUtils.hasLength;

@DisplayName("Сервис для работы с книгами")
@SpringBootTest()
@Transactional(propagation = Propagation.NEVER)
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Возвращать список всех книг")
    @Test
    void shouldFindAllBooks() {
        List<Book> books = bookService.findAll();
        assertThat(books)
                .isNotEmpty()
                .hasSize(3)
                .allMatch(book -> hasLength(book.getTitle()));
    }

    @DisplayName("Создать книгу")
    @Test
    void shouldCreateBook() {
        var expectedTitle = "title_test";
        var expectedAuthorId = 1L;
        Set<Long> expectedGanres = Set.of(1L, 2L);

        Book createdBook = bookService.insert(expectedTitle, expectedAuthorId, expectedGanres);
        var actualBook = bookService.findById(createdBook.getId());
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getAuthor()).isNotNull();
        assertThat(actualBook.get().getGenres()).isNotEmpty();
        assertThat(actualBook.get().getGenres().stream().map(Genre::getId))
                .containsAll(expectedGanres);
        assertThat(actualBook.get().getTitle()).isEqualTo(expectedTitle);
        assertThat(actualBook.get().getAuthor().getId()).isEqualTo(expectedAuthorId);
    }


    @DisplayName("Обновить книгу")
    @Test
    void shouldUpdateBook() {
        var expectedTitle = "title_test";
        var expectedAuthorId = 1L;
        Set<Long> expectedGanres = Set.of(2L, 4L);

        Book prevBook = bookService.findById(1L).get();
        Book editedBook = bookService.update(prevBook.getId(), expectedTitle, expectedAuthorId, expectedGanres);
        Book actualBook = bookService.findById(editedBook.getId()).get();
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

        var actualBook = bookService.findById(1L);
        assertThat(book).isPresent();
        assertThat(actualBook).isEmpty();
    }
}
