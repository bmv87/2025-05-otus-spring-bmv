package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.documents.MongoComment;

public interface MongoCommentRepository extends MongoRepository<MongoComment, Long> {
}
