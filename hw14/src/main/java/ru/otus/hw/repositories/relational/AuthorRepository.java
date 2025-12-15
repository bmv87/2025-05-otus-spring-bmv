package ru.otus.hw.repositories.relational;

import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.otus.hw.entities.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Optional<Author> findByFullNameIgnoreCase(String fullName);
}
