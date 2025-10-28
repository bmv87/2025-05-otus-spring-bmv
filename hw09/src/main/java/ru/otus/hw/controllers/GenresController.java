package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @GetMapping(value = {"/genres/", "/genres"})
    public String findAllGenres(Model model) {
        var genres = genreService.findAll()
                .stream()
                .map(genreConverter::toViewDto)
                .toList();
        model.addAttribute("genres", genres);
        return "genres/list";
    }
}
