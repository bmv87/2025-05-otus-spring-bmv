package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentViewDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/comments"})
public class CommentsController {

    private final CommentService commentService;

    @GetMapping()
    public List<CommentViewDto> getComments(@RequestParam(value = "bookId", required = true) long bookId) {
        return commentService.findByBookId(bookId);
    }

    @PostMapping()
    public CommentViewDto insertToBook(@Valid @RequestBody CommentDto newComment) {
        return commentService.insert(newComment);
    }

    @PutMapping("{id}")
    public CommentViewDto updateComment(@PathVariable("id") long id,
                                        @Valid @RequestBody CommentDto editedComment) {
        return commentService.update(id, editedComment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable long id) {
        commentService.deleteById(id);
    }
}
