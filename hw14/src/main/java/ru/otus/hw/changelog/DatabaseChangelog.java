package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.documents.MongoAuthor;
import ru.otus.hw.documents.MongoBook;
import ru.otus.hw.documents.MongoComment;
import ru.otus.hw.documents.MongoGenre;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.List;
import java.util.stream.IntStream;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "developer", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "developer", runAlways = true)
    public void initData(MongoAuthorRepository repository) {
        var items = getDbAuthors();

        for (var item : items) {
            repository.save(item);
        }
    }

    @ChangeSet(order = "003", id = "initGenres", author = "developer", runAlways = true)
    public void initData(MongoGenreRepository repository) {
        var items = getDbGenres();
        for (var item : items) {
            repository.save(item);
        }
    }

    @ChangeSet(order = "004", id = "initBooks", author = "developer", runAlways = true)
    public void initData(MongoBookRepository repository,
                         MongoAuthorRepository authorRepository,
                         MongoGenreRepository genreRepository) {
        var genres = genreRepository.findAll();
        var authors = authorRepository.findAll();
        var items = getDbBooks(authors, genres);
        for (var item : items) {
            repository.save(item);
        }
    }

    @ChangeSet(order = "005", id = "initComments", author = "developer", runAlways = true)
    public void initData(MongoCommentRepository repository,
                         MongoBookRepository bookRepository) {
        var books = bookRepository.findAll();
        for (var book : books) {
            var items = getDbComments(book);
            for (var item : items) {
                repository.save(item);
            }
        }
    }

    private static List<MongoAuthor> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new MongoAuthor(Long.valueOf(id), "Author_" + id))
                .toList();
    }

    private static List<MongoGenre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new MongoGenre(Long.valueOf(id), "Genre_" + id))
                .toList();
    }

    private static List<MongoComment> getDbComments(MongoBook book) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new MongoComment(Long.valueOf(id),
                        "content " + book.getId() + "_" + id, book))
                .toList();
    }

    private static List<MongoBook> getDbBooks(List<MongoAuthor> dbAuthors, List<MongoGenre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new MongoBook(Long.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }
}
