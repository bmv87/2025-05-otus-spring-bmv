package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentViewDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.security.PermissionGroup;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclServiceWrapperService;

    private Map<PermissionGroup, List<Permission>> getPermissionConfig() {
        return Map.of(
                PermissionGroup.CURRENT, List.of(BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE),
                PermissionGroup.ADMIN, List.of(BasePermission.READ, BasePermission.DELETE));
    }

    @Override
    public CommentViewDto findById(long id) {
        var comment = commentRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        return commentMapper.toViewDto(comment);
    }

    @Override
    public List<CommentViewDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId)
                .stream()
                .map(commentMapper::toViewDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentViewDto insert(CommentDto newComment) {
        if (newComment.getBookId() == 0L) {
            throw new IllegalArgumentException("Book id not valid");
        }
        validateComment(newComment.getContent());

        var book = bookRepository.findById(newComment.getBookId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %d not found".formatted(newComment.getBookId())));
        var comment = new Comment(0, newComment.getContent(), book);
        comment = commentRepository.save(comment);
        aclServiceWrapperService.createPermission(comment, getPermissionConfig());
        return commentMapper.toViewDto(comment);
    }

    @Override
    @Transactional
    @PreAuthorize("canWrite(#id, T(ru.otus.hw.models.Comment))")
    public CommentViewDto update(long id, CommentDto editedComment) {
        if (id == 0L) {
            throw new IllegalArgumentException("Comment id not valid");
        }
        validateComment(editedComment.getContent());

        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setContent(editedComment.getContent());
        comment = commentRepository.save(comment);
        return commentMapper.toViewDto(comment);
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
