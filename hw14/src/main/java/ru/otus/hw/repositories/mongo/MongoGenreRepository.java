package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.documents.MongoGenre;

public interface MongoGenreRepository extends MongoRepository<MongoGenre, Long> {
}
