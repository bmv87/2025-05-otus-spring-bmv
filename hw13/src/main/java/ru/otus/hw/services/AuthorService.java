package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorViewDto;

import java.util.List;

public interface AuthorService {
    List<AuthorViewDto> findAll();
}
