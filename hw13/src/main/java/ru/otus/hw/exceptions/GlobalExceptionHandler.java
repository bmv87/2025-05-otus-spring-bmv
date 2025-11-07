package ru.otus.hw.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.dto.FieldErrorDto;

import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handeNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        var error = new ErrorDto("404", ex.getMessage(), null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDto> handeException(BindException ex) {
        log.error(ex.getMessage(), ex);
        List<FieldErrorDto> errors = null;
        if (ex.getBindingResult().hasFieldErrors()) {
            errors = ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                    .toList();
        }
        var error = new ErrorDto("400", "Validation error", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handeException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        var error = new ErrorDto("403", "Access denied", null);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handeException(Exception ex) {
        log.error(ex.getMessage(), ex);
        var error = new ErrorDto("500", "Unexpectable error", null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
