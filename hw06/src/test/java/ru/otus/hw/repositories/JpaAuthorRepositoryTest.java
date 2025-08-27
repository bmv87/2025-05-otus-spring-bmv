package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать весь список авторов")
    @Test
    void shouldReturnAllAuthors() {
        var expectedAuthors = getDbAuthors();
        var actualAuthors = repositoryJpa.findAll();
        assertThat(actualAuthors).isNotEmpty().containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectBookById(Author expectedAuthor) {
        var actualBook = repositoryJpa.findById(expectedAuthor.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    private static List<Author> getAuthors(int startRange, int endRange) {
        return IntStream.range(startRange, endRange).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Author> getDbAuthors() {
        return getAuthors(1, 4);
    }
}