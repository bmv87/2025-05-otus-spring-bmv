package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@JdbcTest
@Import({JdbcGenreRepository.class})
class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository repositoryJdbc;

    @DisplayName("должен загружать весь список жанров")
    @Test
    void shouldReturnAllGenres() {
        var expectedGenres = getDbGenres(1, 7);
        var actualGenres = repositoryJdbc.findAll();
        assertThat(actualGenres).isNotEmpty().containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать список жанров по указанному сприску id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        var expectedGenres = getDbGenres(2, 4);
        var ids = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        var actualGenres = repositoryJdbc.findAllByIds(ids);
        assertThat(actualGenres).isNotEmpty().isEqualTo(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    private static List<Genre> getDbGenres(int startRange, int endRange) {
        return IntStream.range(startRange, endRange).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}