package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentById(Comment expectedComment) {
        var actualComment = repositoryJpa.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        var book = getDbBooks().get(0);
        var actualComments = repositoryJpa.findByBookId(book.getId());
        var expectedComments = getDbComments(book);

        assertThat(actualComments)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("book")
                .containsAll(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(0, "content_3_1", getDbBooks().get(2));
        var returnedComment = repositoryJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repositoryJpa.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(1L, "content_1_3", getDbBooks().get(0));

        assertThat(repositoryJpa.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isNotEqualTo(expectedComment);

        var returnedComment = repositoryJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repositoryJpa.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        var comment = repositoryJpa.findById(1L).orElse(null);
        assertThat(comment).isNotNull();
        repositoryJpa.deleteById(1L);

        assertThat(repositoryJpa.findById(1L)).isEmpty();
    }

    private static List<Comment> getDbComments(Book book) {
        return IntStream.range(1, 3).boxed()
                .map(id -> new Comment(id,
                        "content " + book.getId() + "_" + id,
                        null
                ))
                .toList();
    }

    private static List<Comment> getDbComments() {
        var book = getDbBooks().get(0);
        return getDbComments(book);
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