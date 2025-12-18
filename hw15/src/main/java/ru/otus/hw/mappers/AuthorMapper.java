package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorViewDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorMapper {
    public AuthorViewDto toViewDto(Author author) {
        return new AuthorViewDto(author.getId(), author.getFullName());
    }
}
