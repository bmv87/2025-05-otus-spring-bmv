package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookViewDto {

    private long id;

    private String title;

    private AuthorViewDto author;

    private List<GenreViewDto> genres = new ArrayList<>();
}
