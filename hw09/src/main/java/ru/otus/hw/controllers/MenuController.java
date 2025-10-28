package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MenuController {

    @GetMapping(value = {"", "/", "/menu"})
    public String findAllBooks() {
        return "menu";
    }

}
