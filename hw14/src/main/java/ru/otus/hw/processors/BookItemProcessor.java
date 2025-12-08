package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.cache.CacheHelper;
import ru.otus.hw.documents.MongoAuthor;
import ru.otus.hw.documents.MongoBook;
import ru.otus.hw.documents.MongoGenre;
import ru.otus.hw.entities.Author;
import ru.otus.hw.entities.Book;
import ru.otus.hw.entities.Genre;
import ru.otus.hw.models.ItemBox;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookItemProcessor implements
        ItemProcessor<MongoBook, ItemBox<Book, Long, Long>> {

    private final CacheHelper cacheHelper;

    @Override
    public ItemBox<Book, Long, Long> process(MongoBook item) throws Exception {
        var author = getOrInitAuthor(item.getAuthor());
        var genres = getOrInitGenres(item.getGenres());
        var boxedBook = initBook(item.getTitle(), author, genres);
        boxedBook.setInKey(item.getId());
        return boxedBook;
    }

    private Author getOrInitAuthor(MongoAuthor author) {
        Long dbOutId = cacheHelper.tryGetId(CacheHelper.AUTHORS, author.getId());
        if (dbOutId == null) {
            return new Author(0L, author.getFullName());
        }
        return new Author(dbOutId, author.getFullName());
    }

    private List<Genre> getOrInitGenres(List<MongoGenre> mongoGenres) {
        var genres = new ArrayList<Genre>();
        for (var genre : mongoGenres) {
            Long dbOutId = cacheHelper.tryGetId(CacheHelper.GENRES, genre.getId());
            if (dbOutId == null) {
                genres.add(new Genre(0L, genre.getName()));
                continue;
            }
            genres.add(new Genre(dbOutId, genre.getName()));
        }

        return genres;
    }

    private ItemBox<Book, Long, Long> initBook(String title, Author author, List<Genre> genre) {
        return new ItemBox<>(new Book(0L, title, author, genre, null));
    }
}
