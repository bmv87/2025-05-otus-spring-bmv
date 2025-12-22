package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookViewDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto toListItemDto(Book book) {
        var genresDtoList = book.getGenres().stream().map(genreMapper::toViewDto).toList();
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorMapper.toViewDto(book.getAuthor()),
                genresDtoList);
    }

    public BookCreateDto toUpdateDto(Book book) {
        var genresIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        return new BookCreateDto(
                book.getTitle(),
                book.getAuthor().getId(),
                genresIds);
    }

    public BookViewDto toBookViewDto(Book book) {
        var genresDtoList = book.getGenres()
                .stream()
                .map(genreMapper::toViewDto)
                .toList();

        return new BookViewDto(
                book.getId(),
                book.getTitle(),
                authorMapper.toViewDto(book.getAuthor()),
                genresDtoList
        );
    }
}
