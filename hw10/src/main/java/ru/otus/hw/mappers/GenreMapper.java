package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreViewDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapper {
    public GenreViewDto toViewDto(Genre genre) {
        return new GenreViewDto(genre.getId(), genre.getName());
    }
}
