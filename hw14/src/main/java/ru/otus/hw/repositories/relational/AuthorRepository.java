package ru.otus.hw.repositories.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.entities.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFullName(String fullName);
}
