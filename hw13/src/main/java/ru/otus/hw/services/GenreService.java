package ru.otus.hw.services;

import ru.otus.hw.dto.GenreViewDto;

import java.util.List;

public interface GenreService {
    List<GenreViewDto> findAll();
}
