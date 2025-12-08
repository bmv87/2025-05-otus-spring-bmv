package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.cache.CacheHelper;
import ru.otus.hw.documents.MongoComment;
import ru.otus.hw.entities.Book;
import ru.otus.hw.entities.Comment;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.repositories.relational.BookRepository;

@Component
@RequiredArgsConstructor
public class CommentItemProcessor implements ItemProcessor<MongoComment, Comment> {

    private final CacheHelper cacheHelper;

    private final BookRepository bookRepository;

    @Override
    public Comment process(MongoComment item) throws Exception {
        var book = getBook(item.getBook().getId());
        return new Comment(0, item.getContent(), book);
    }

    private Book getBook(Long mongoBookId) {
        var bookId = cacheHelper.tryGetId(CacheHelper.BOOKS, mongoBookId);
        if (bookId == null) {
            throw new NotFoundException(String.format("Book not found in cache by key %d ", mongoBookId));
        }
        var book = new Book();
        book.setId(bookId);
        return book;
    }
}
