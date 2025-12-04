package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.documents.MongoAuthor;
import ru.otus.hw.entities.Author;
import ru.otus.hw.repositories.relational.AuthorRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorItemProcessor implements ItemProcessor<MongoAuthor, Author> {

    private final AuthorRepository authorRepository;

    @Override
    public Author process(MongoAuthor item) throws Exception {
        return getOrInitAuthor(item.getFullName());
    }

    private Author getOrInitAuthor(String name) {
        Optional<Author> authorOptional = authorRepository.findByFullNameIgnoreCase(name);
        return authorOptional.orElse(new Author(0, name));
    }
}
