package ru.otus.hw.repositories.relational;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
