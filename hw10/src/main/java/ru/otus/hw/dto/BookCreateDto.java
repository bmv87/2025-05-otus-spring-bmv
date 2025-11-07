package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateDto {

    @NotBlank()
    @Size(min = 2, max = 250)
    private String title;

    @NotNull
    private Long author;

    @NotEmpty
    private Set<@NotNull Long> genres = new HashSet<>();
}
