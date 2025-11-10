package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorViewDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorConverter {
    public AuthorViewDto toViewDto(Author author) {
        return new AuthorViewDto(author.getId(), author.getFullName());
    }
}
