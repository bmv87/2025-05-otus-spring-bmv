package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.documents.MongoAuthor;
import ru.otus.hw.documents.MongoComment;
import ru.otus.hw.entities.Book;
import ru.otus.hw.entities.Comment;
import ru.otus.hw.repositories.relational.BookRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentItemProcessor implements ItemProcessor<MongoComment, Comment> {

    private final BookRepository bookRepository;

    @Override
    public Comment process(MongoComment item) throws Exception {
        var book = getBook(item.getBook().getTitle(), item.getBook().getAuthor());
        return new Comment(0, item.getContent(), book);
    }

    private Book getBook(String title, MongoAuthor author) {
        Optional<Book> bookOptional = bookRepository.findByTitleAndAuthorFullName(title, author.getFullName());
        return bookOptional.get();
    }
}
