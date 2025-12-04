package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.documents.MongoBook;
import ru.otus.hw.documents.MongoGenre;
import ru.otus.hw.entities.Author;
import ru.otus.hw.entities.Book;
import ru.otus.hw.entities.Genre;
import ru.otus.hw.repositories.relational.AuthorRepository;
import ru.otus.hw.repositories.relational.BookRepository;
import ru.otus.hw.repositories.relational.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookItemProcessor implements ItemProcessor<MongoBook, Book> {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Book process(MongoBook item) throws Exception {
        var author = getOrInitAuthor(item.getAuthor().getFullName());
        var genres = getOrInitGenres(item.getGenres().stream().map(MongoGenre::getName).toList());
        var book = getOrInitBook(item.getTitle(), author, genres);
        return book;
    }

    private Author getOrInitAuthor(String name) {
        Optional<Author> authorOptional = authorRepository.findByFullNameIgnoreCase(name.toUpperCase());
        return authorOptional.orElse(new Author(0, name));
    }

    private List<Genre> getOrInitGenres(List<String> names) {
        var genres = new ArrayList<Genre>();
        for (var name : names) {
            var genre = genreRepository.findByNameIgnoreCase(name.toUpperCase());
            if (genre.isEmpty()) {
                genres.add(new Genre(0, name));
            }
        }

        return genres;
    }

    private Book getOrInitBook(String title, Author author, List<Genre> genre) {
        Optional<Book> bookOptional = bookRepository.findByTitleAndAuthorFullName(title, author.getFullName());
        return bookOptional.orElse(new Book(0, title, author, genre, null));
    }
}
