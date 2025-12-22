package ru.otus.hw.controllers;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorViewDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/authors"})
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping()
    @Timed(
            value = "long.timer.allAuthors",
            longTask = true,
            description = "Get all authors timer"
    )
    public List<AuthorViewDto> findAllAuthors() {
        return authorService.findAll();
    }
}
