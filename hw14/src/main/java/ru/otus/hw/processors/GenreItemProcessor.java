package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.documents.MongoGenre;
import ru.otus.hw.entities.Genre;
import ru.otus.hw.repositories.relational.GenreRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreItemProcessor implements ItemProcessor<MongoGenre, Genre> {

    private final GenreRepository genreRepository;

    @Override
    public Genre process(MongoGenre item) throws Exception {
        return getOrInitGenre(item.getName());
    }

    private Genre getOrInitGenre(String name) {
        Optional<Genre> authorOptional = genreRepository.findByNameIgnoreCase(name);
        return authorOptional.orElse(new Genre(0, name));
    }
}
