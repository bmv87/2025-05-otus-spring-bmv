package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.documents.MongoGenre;
import ru.otus.hw.entities.Genre;
import ru.otus.hw.models.ItemBox;

@Component
@RequiredArgsConstructor
public class GenreItemProcessor implements
        ItemProcessor<MongoGenre, ItemBox<Genre, Long, Long>> {

    @Override
    public ItemBox<Genre, Long, Long> process(MongoGenre item) throws Exception {
        return new ItemBox<>(item.getId(), new Genre(0L, item.getName()));
    }
}
