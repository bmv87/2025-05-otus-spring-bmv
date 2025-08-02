package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcOperations.query(
                "select id, full_name from authors", new AuthorRowMapper()
        );
    }

    @Override
    public Optional<Author> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Author result = null;
        try {
            result = namedParameterJdbcOperations.queryForObject(
                    "select id, full_name from authors where id = :id", params, new AuthorRowMapper()
            );
        } catch (DataAccessException ex) {
            log.error("findById method error " + ex.getMessage(), ex);
        }

        return Optional.ofNullable(result);
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
