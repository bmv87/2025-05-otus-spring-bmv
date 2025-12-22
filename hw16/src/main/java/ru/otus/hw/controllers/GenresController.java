package ru.otus.hw.controllers;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreViewDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/genres"})
public class GenresController {

    private final GenreService genreService;

    @GetMapping()
    @Timed(
            value = "long.timer.allGenres",
            longTask = true,
            description = "Get all genres timer"
    )
    public List<GenreViewDto> findAllGenres(Model model) {
        return genreService.findAll();
    }
}
