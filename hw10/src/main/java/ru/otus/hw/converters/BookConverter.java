package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.BookViewDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public BookDto toListItemDto(Book book) {
        var genresDtoList = book.getGenres().stream().map(genreConverter::toViewDto).toList();
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorConverter.toViewDto(book.getAuthor()),
                genresDtoList);
    }

    public BookUpdateDto toUpdateDto(Book book) {
        var genresIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        return new BookUpdateDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                genresIds);
    }

    public BookViewDto toBookViewDto(Book book) {
        var genresDtoList = book.getGenres()
                .stream()
                .map(genreConverter::toViewDto)
                .toList();

        return new BookViewDto(
                book.getId(),
                book.getTitle(),
                authorConverter.toViewDto(book.getAuthor()),
                genresDtoList
        );
    }
}
