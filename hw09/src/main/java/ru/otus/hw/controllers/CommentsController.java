package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentEditDto;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "comments")
public class CommentsController {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @GetMapping("/create")
    public String insertToBook(@RequestParam(value = "bookId", required = true) long bookId, Model model) {
        model.addAttribute("comment", new CommentDto(bookId, null));
        return "comments/create";
    }

    @PostMapping("/create")
    public String insertToBook(@Valid @ModelAttribute("comment") CommentDto newComment,
                               BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return "comments/create";
        }
        commentService.insert(newComment.getBookId(), newComment.getContent());
        return "redirect:/books/view/" + newComment.getBookId();
    }

    @GetMapping("/edit/{id}")
    public String updateComment(@PathVariable("id") long id, Model model) {
        var comment = commentService.findById(id);
        var commentDto = commentConverter.toEditDto(comment);
        model.addAttribute("comment", commentDto);
        return "comments/edit";
    }

    @PutMapping("/edit")
    public String updateComment(@Valid @ModelAttribute("comment") CommentEditDto editedComment,
                                BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return "comments/edit";
        }
        commentService.update(editedComment.getId(), editedComment.getContent());
        return "redirect:/books/view/" + editedComment.getBookId();
    }

    @DeleteMapping("{id}")
    public String deleteComment(@PathVariable long id, long bookId) {
        commentService.deleteById(id);
        return "redirect:/books/view/" + bookId;
    }
}
