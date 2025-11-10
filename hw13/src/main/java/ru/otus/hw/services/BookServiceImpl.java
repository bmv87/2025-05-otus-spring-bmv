package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookViewDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.security.PermissionGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclServiceWrapperService;

    private Map<PermissionGroup, List<Permission>> getPermissionConfig() {
        return Map.of(
                PermissionGroup.CURRENT, List.of(BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE),
                PermissionGroup.ADMIN, List.of(BasePermission.ADMINISTRATION));
    }

    @Override
    @Transactional
    public BookViewDto findById(long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return bookMapper.toBookViewDto(book);
    }

    @Override
    @Transactional
    public List<BookDto> findAll() {
        var books = bookRepository.findAll()
                .stream()
                .map(bookMapper::toListItemDto)
                .toList();
        return books;
    }

    @Override
    @Transactional
    public BookViewDto insert(BookCreateDto bookDto) {
        var book = new Book();
        book.setId(0);
        validateAndFill(book, bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());
        book = bookRepository.save(book);
        aclServiceWrapperService.createPermission(book, getPermissionConfig());
        return bookMapper.toBookViewDto(book);
    }

    @Override
    @Transactional
    @PreAuthorize("canWrite(#id, T(ru.otus.hw.models.Book))")
    public BookViewDto update(long id, BookCreateDto bookDto) {
        var book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %d not found".formatted(id)));

        validateAndFill(book, bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());
        book = bookRepository.save(book);
        return bookMapper.toBookViewDto(book);
    }

    @Override
    @Transactional
    @PreAuthorize("canDelete(#id, T(ru.otus.hw.models.Book))")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private void validateAndFill(Book book, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllById(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);
    }
}
