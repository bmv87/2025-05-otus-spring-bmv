package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorDto {

    private String code;

    private String message;

    private List<FieldErrorDto> fieldsErrors;

}
