package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами")
@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать весь список жанров")
    @Test
    void shouldReturnAllGenres() {
        var expectedGenres = getDbGenres(1, 7);
        var actualGenres = repositoryJpa.findAll();
        assertThat(actualGenres)
                .isNotEmpty()
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать список жанров по указанному списку id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        var expectedGenres = getDbGenres(2, 4);
        var ids = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        var actualGenres = repositoryJpa.findAllById(ids);
        assertThat(actualGenres)
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    private static List<Genre> getDbGenres(int startRange, int endRange) {
        return IntStream.range(startRange, endRange).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}