package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreViewDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/genres"})
public class GenresController {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @GetMapping()
    public List<GenreViewDto> findAllGenres(Model model) {
        var genres = genreService.findAll()
                .stream()
                .map(genreConverter::toViewDto)
                .toList();
        return genres;
    }
}
