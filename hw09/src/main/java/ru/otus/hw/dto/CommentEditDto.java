package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEditDto {
    @NotNull
    private Long id;

    @NotNull
    private Long bookId;

    @NotBlank()
    @Size(min = 2, max = 250)
    private String content;
}
