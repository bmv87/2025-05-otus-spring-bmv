package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@ConditionalOnEnabledHealthIndicator("external")
@RequiredArgsConstructor
@Slf4j
public class ExternalHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        try {
            var bookCount = bookRepository.count();
            if (bookCount == 0) {
                return new Health.Builder().down().withDetail("books", bookCount).build();
            }

            return new Health.Builder().up().withDetail("books", bookCount).build();
        } catch (Exception ex) {
            log.error("Book repository error", ex);
            return new Health.Builder().down().withDetail("repository error", "see logfile").build();
        }
    }
}
