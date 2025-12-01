package ru.otus.hw.repositories.relational;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.entities.Book;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findByTitleAndAuthorFullName(String title, String fullName);
}

