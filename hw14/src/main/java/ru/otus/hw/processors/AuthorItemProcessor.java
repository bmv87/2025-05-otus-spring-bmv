package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.documents.MongoAuthor;
import ru.otus.hw.entities.Author;
import ru.otus.hw.models.ItemBox;

@Component
@RequiredArgsConstructor
public class AuthorItemProcessor implements
        ItemProcessor<MongoAuthor, ItemBox<Author, Long, Long>> {

    @Override
    public ItemBox<Author, Long, Long> process(MongoAuthor item) throws Exception {
        return new ItemBox<>(item.getId(), new Author(0L, item.getFullName()));
    }
}
