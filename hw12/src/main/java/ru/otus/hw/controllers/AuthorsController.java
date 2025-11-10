package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorViewDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/authors"})
public class AuthorsController {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @GetMapping()
    public List<AuthorViewDto> findAllAuthors() {
        var authors = authorService.findAll()
                .stream()
                .map(authorConverter::toViewDto)
                .toList();
        return authors;
    }
}
