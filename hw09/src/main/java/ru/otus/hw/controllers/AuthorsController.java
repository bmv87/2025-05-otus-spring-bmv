package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.services.AuthorService;

@Controller
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @GetMapping(value = {"/authors/", "/authors"})
    public String findAllAuthors(Model model) {
        var authors = authorService.findAll()
                .stream()
                .map(authorConverter::toViewDto)
                .toList();
        model.addAttribute("authors", authors);
        return "authors/list";
    }
}
