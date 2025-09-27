package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

@DataMongoTest
@ComponentScan({"ru.otus.hw", "ru.otus.hw.repositories"})
public abstract class AbstractRepositoryTest {

    protected List<Author> dbAuthors;

    protected List<Genre> dbGenres;

    protected List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks();
    }


    protected static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(Long.valueOf(id), "Author_" + id))
                .toList();
    }

    protected static List<Genre> getDbGenres() {
        return getDbGenres(1, 7);
    }

    protected static List<Genre> getDbGenres(int startRange, int endRange) {
        return IntStream.range(startRange, endRange).boxed()
                .map(id -> new Genre(Long.valueOf(id), "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(int startRange, int endRange) {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return IntStream.range(startRange, endRange).boxed()
                .map(id -> new Book(Long.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    protected static List<Book> getDbBooks() {
        return getDbBooks(1, 4);
    }

    private static List<Comment> getDbComments(int startRange, int endRange, Book book) {
        return IntStream.range(startRange, endRange).boxed()
                .map(id -> new Comment(Long.valueOf(id),
                        "content " + book.getId() + "_" + id,
                        null
                ))
                .toList();
    }

    protected static List<Comment> getDbComments(Book book) {
        return getDbComments(1, 3, book);
    }

    protected static List<Comment> getDbComments() {
        var book = getDbBooks().get(0);
        return getDbComments(1, 3, book).stream().limit(2).toList();
    }
}
