package ru.otus.hw.repositories.relational;

import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.entities.Book;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Optional<Book> findByTitleAndAuthorFullName(String title, String fullName);
}

