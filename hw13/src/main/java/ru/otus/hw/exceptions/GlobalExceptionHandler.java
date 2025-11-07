package ru.otus.hw.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.dto.FieldErrorDto;

import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handeNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return new ErrorDto("404", ex.getMessage(), null);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handeException(BindException ex) {
        log.error(ex.getMessage());
        List<FieldErrorDto> errors = null;
        if (ex.getBindingResult().hasFieldErrors()) {
            errors = ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                    .toList();
        }
        return new ErrorDto("400", "Validation error", errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handeException(AccessDeniedException ex) {
        log.error(ex.getMessage());
        return new ErrorDto("403", "Access denied", null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handeException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto("500", "Unexpectable error", null);
    }
}
