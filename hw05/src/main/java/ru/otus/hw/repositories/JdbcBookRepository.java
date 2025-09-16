package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        Book result = null;

        result = namedParameterJdbcOperations.query(
                "select b.id, title, author_id, full_name as author_full_name, genre_id, name as genre_name " +
                        "from books b " +
                        "left join authors a on b.author_id = a.id " +
                        "left join books_genres bg on b.id = bg.book_id " +
                        "left join genres g on g.id = bg.genre_id " +
                        "where b.id = :id", params, new BookResultSetExtractor()
        );

        return Optional.ofNullable(result);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);

        namedParameterJdbcOperations.update(
                "delete from books where id = :id", params
        );
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcOperations.query(
                "select b.id, title, author_id, a.full_name as author_full_name " +
                        "from books b " +
                        "left join authors a on b.author_id = a.id", new BookRowMapper()
        );
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedParameterJdbcOperations.query(
                "select book_id, genre_id from books_genres", new BookGenreRelationRowMapper()
        );
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        var relationsMap = relations.stream()
                .collect(
                        groupingBy(BookGenreRelation::bookId,
                                mapping(BookGenreRelation::genreId, toList())));
        var bookGenresMap = genres.stream()
                .collect(toMap(Genre::getId, g -> g));
        booksWithoutGenres.forEach(book -> {
            var bookGenresIds = relationsMap.getOrDefault(book.getId(), List.of());
            if (bookGenresIds.isEmpty()) {
                return;
            }
            var bookGenres = bookGenresIds.stream().map(bookGenresMap::get)
                    .collect(toList());

            book.setGenres(bookGenres);
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
                .addValue("authorId", book.getAuthor().getId())
                .addValue("title", book.getTitle());

        //noinspection DataFlowIssue

        namedParameterJdbcOperations.update(
                "insert into books (title, author_id) values (:title, :authorId)",
                params,
                keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        var params = new MapSqlParameterSource()
                .addValue("authorId", book.getAuthor().getId())
                .addValue("title", book.getTitle())
                .addValue("id", book.getId());
        var count = namedParameterJdbcOperations.update(
                "update books " +
                        "set title = :title, author_id = :authorId " +
                        "where id = :id", params
        );
        if (count == 0) {
            throw new EntityNotFoundException(String.format("Book with id %d not found", book.getId()));
        }
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        List<BookGenreRelation> relations = book.getGenres().stream()
                .map(g -> new BookGenreRelation(book.getId(), g.getId()))
                .toList();
        var params = SqlParameterSourceUtils.createBatch(relations);
        namedParameterJdbcOperations.batchUpdate(
                "insert into books_genres (book_id, genre_id) values (:bookId, :genreId)",
                params);
    }

    private void removeGenresRelationsFor(Book book) {
        Map<String, Object> params = Collections.singletonMap("id", book.getId());

        namedParameterJdbcOperations.update(
                "delete from books_genres where book_id = :id", params
        );
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var book = new Book();
            long id = rs.getLong("id");
            book.setId(id);
            String title = rs.getString("title");
            book.setTitle(title);
            Long authorId = rs.getLong("author_id");
            if (!rs.wasNull()) {
                String fullName = rs.getString("author_full_name");
                book.setAuthor(new Author(authorId, fullName));
            }
            book.setGenres(new ArrayList<>());
            return book;
        }
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");

            return new BookGenreRelation(bookId, genreId);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            List<Genre> genres = new ArrayList<>();
            while (rs.next()) {
                if (book == null) {
                    book = new BookRowMapper().mapRow(rs, rs.getRow());
                }
                long genreId = rs.getLong("genre_id");
                String genreName = rs.getString("genre_name");
                genres.add(new Genre(genreId, genreName));
            }
            if (book == null) {
                return null;
            }
            book.setGenres(genres);
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
