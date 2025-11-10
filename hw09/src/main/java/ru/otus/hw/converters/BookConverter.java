package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.dto.BookListItemDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public BookListItemDto toListItemDto(Book book) {
        var genresDtoList = book.getGenres().stream().map(genreConverter::toViewDto).toList();
        return new BookListItemDto(
                book.getId(),
                book.getTitle(),
                authorConverter.toViewDto(book.getAuthor()),
                genresDtoList);
    }

    public BookEditDto toEditDto(Book book) {
        var genresIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        return new BookEditDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                genresIds);
    }

    public BookWithCommentsDto toBookWithCommentsDto(Book book) {
        var genresDtoList = book.getGenres()
                .stream()
                .map(genreConverter::toViewDto)
                .toList();
        var commentsDtoList = book.getComments()
                .stream()
                .map(commentConverter::toViewDto)
                .toList();

        return new BookWithCommentsDto(
                book.getId(),
                book.getTitle(),
                authorConverter.toViewDto(book.getAuthor()),
                genresDtoList,
                commentsDtoList
        );
    }
}
