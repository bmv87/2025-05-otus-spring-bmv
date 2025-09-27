package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.sequences.SequenceGeneratorService;

import java.util.List;
import java.util.stream.IntStream;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "developer", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "developer", runAlways = true)
    public void initData(AuthorRepository repository, SequenceGeneratorService sequenceGeneratorService) {
        var items = getDbAuthors();
        for (var item : items) {
            item.setId(sequenceGeneratorService.generateSequence(Author.SEQUENCE_NAME));
            repository.save(item);
        }
    }

    @ChangeSet(order = "003", id = "initGenres", author = "developer", runAlways = true)
    public void initData(GenreRepository repository, SequenceGeneratorService sequenceGeneratorService) {
        var items = getDbGenres();
        for (var item : items) {
            item.setId(sequenceGeneratorService.generateSequence(Genre.SEQUENCE_NAME));
            repository.save(item);
        }
    }

    @ChangeSet(order = "004", id = "initBooks", author = "developer", runAlways = true)
    public void initData(BookRepository repository,
                         AuthorRepository authorRepository,
                         GenreRepository genreRepository,
                         SequenceGeneratorService sequenceGeneratorService) {
        var genres = genreRepository.findAll();
        var authors = authorRepository.findAll();
        var items = getDbBooks(authors, genres);
        for (var item : items) {
            item.setId(sequenceGeneratorService.generateSequence(Book.SEQUENCE_NAME));
            repository.save(item);
        }
    }

    @ChangeSet(order = "005", id = "initComments", author = "developer", runAlways = true)
    public void initData(CommentRepository repository,
                         BookRepository bookRepository,
                         SequenceGeneratorService sequenceGeneratorService) {
        var books = bookRepository.findAll();
        for (var book : books) {
            var items = getDbComments(book);
            for (var item : items) {
                item.setId(sequenceGeneratorService.generateSequence(Comment.SEQUENCE_NAME));
                repository.save(item);
            }
        }
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(Long.valueOf(id), "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(Long.valueOf(id), "Genre_" + id))
                .toList();
    }

    private static List<Comment> getDbComments(Book book) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Comment(Long.valueOf(id),
                        "content " + book.getId() + "_" + id, book))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(Long.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }
}
