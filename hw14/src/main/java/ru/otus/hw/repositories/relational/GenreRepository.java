package ru.otus.hw.repositories.relational;

import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.otus.hw.entities.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Optional<Genre> findByNameIgnoreCase(String name);
}
