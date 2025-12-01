package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.documents.MongoBook;

public interface MongoBookRepository extends MongoRepository<MongoBook, Long> {

}

