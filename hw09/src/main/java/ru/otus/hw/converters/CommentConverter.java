package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentEditDto;
import ru.otus.hw.dto.CommentViewDto;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {
    public CommentViewDto toViewDto(Comment comment) {
        return new CommentViewDto(comment.getId(), comment.getBook().getId(), comment.getContent());
    }

    public CommentEditDto toEditDto(Comment comment) {
        return new CommentEditDto(comment.getId(), comment.getBook().getId(), comment.getContent());
    }
}
