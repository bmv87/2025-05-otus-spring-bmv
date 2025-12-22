package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.mappers.BookMapper;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.otus.hw.security.AuthorityConstants.AUTHOR;

@DisplayName("Сервис для работы с книгами (Проверка ACL доступов)")
@SpringBootTest()
@Transactional(propagation = Propagation.NEVER)
public class BookServiceImplAccessTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookMapper bookMapper;

    @DisplayName("Чужую книгу автор обновить не может")
    @Test
    @WithMockUser(username = "author", roles = {AUTHOR})
    void shouldThrowAccessDeniedExceptionForUpdateNotOwnBook() {
        var expectedTitle = "title_test";
        var expectedAuthorId = 1L;
        Set<Long> expectedGanres = Set.of(2L, 4L);

        var prevBook = new BookCreateDto(expectedTitle, expectedAuthorId, expectedGanres);
        assertThatThrownBy(() -> {
            bookService.update(1L, prevBook);
        }).isInstanceOf(AccessDeniedException.class);
    }
}
