package ru.otus.hw.mappers;

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
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    private final CommentMapper commentMapper;

    public BookDto toListItemDto(Book book) {
        var genresDtoList = book.getGenres().stream().map(genreMapper::toViewDto).toList();
        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorMapper.toViewDto(book.getAuthor()),
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
