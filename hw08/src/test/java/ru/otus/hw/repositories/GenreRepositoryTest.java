package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий  для работы с жанрами")
@DataMongoTest
class GenreRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("должен загружать весь список жанров")
    @Test
    void shouldReturnAllGenres() {
        var expectedGenres = getDbGenres(1, 7);
        var actualGenres = repository.findAll();
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
        var actualGenres = repository.findAllById(ids);
        assertThat(actualGenres)
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
        actualGenres.forEach(System.out::println);
    }
}