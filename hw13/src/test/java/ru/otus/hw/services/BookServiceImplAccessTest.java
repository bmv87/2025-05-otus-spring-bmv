package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.otus.hw.security.AuthorityConstants.AUTHOR;

@DisplayName("Сервис для работы с книгами (Проверка ACL доступов)")
@SpringBootTest()
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceImplAccessTest {

    @Autowired
    private BookServiceImpl bookService;


    @DisplayName("Чужую книгу автор обновить не может")
    @Test
    @WithMockUser(username = "author", roles = {AUTHOR})
    void shouldThrowAccessDeniedExceptionForUpdateNotOwnBook() {
        var expectedTitle = "title_test";
        var expectedAuthorId = 1L;
        Set<Long> expectedGanres = Set.of(2L, 4L);

        Book prevBook = bookService.findById(1L);
        assertThatThrownBy(() -> {
            bookService.update(prevBook.getId(), expectedTitle, expectedAuthorId, expectedGanres);
        }).isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("Чужую книгу автор удалить не может")
    @Test
    @WithMockUser(username = "author", roles = {AUTHOR})
    void shouldThrowAccessDeniedExceptionForDeleteNotOwnBook() {
        var book = bookService.findById(1L);

        assertThat(book).isNotNull();
        assertThatThrownBy(() -> {
            bookService.deleteById(1L);
        }).isInstanceOf(AccessDeniedException.class);
    }
}
