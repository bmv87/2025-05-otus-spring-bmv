package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами ")
class AuthorRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @DisplayName("должен загружать весь список авторов")
    @Test
    void shouldReturnAllAuthors() {
        var expectedAuthors = getDbAuthors();
        var actualAuthors = repository.findAll();
        assertThat(actualAuthors).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectBookById(Author expectedAuthor) {
        var actualBook = repository.findById(expectedAuthor.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

}