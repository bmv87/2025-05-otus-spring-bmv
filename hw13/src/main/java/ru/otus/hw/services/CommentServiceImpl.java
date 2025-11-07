package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.security.PermissionGroup;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclServiceWrapperService;

    private Map<PermissionGroup, List<Permission>> getPermissionConfig() {
        return Map.of(
                PermissionGroup.CURRENT, List.of(BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE),
                PermissionGroup.ADMIN, List.of(BasePermission.READ, BasePermission.DELETE));
    }

    @Override
    public Comment findById(long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(long bookId, String content) {
        if (bookId == 0L) {
            throw new IllegalArgumentException("Book id not valid");
        }
        validateComment(content);

        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(0, content, book);
        comment = commentRepository.save(comment);
        aclServiceWrapperService.createPermission(comment, getPermissionConfig());
        return comment;
    }

    @Override
    @Transactional
    @PreAuthorize("canWrite(#id, T(ru.otus.hw.models.Comment))")
    public Comment update(long id, String content) {
        if (id == 0L) {
            throw new IllegalArgumentException("Comment id not valid");
        }
        validateComment(content);

        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    @PreAuthorize("canDelete(#id, T(ru.otus.hw.models.Comment))")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private void validateComment(String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("Comment must not be empty");
        }
    }
}
