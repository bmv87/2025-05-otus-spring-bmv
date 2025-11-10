package ru.otus.hw.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.dto.FieldErrorDto;

import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handeNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return new ModelAndView("error",
                "error", new ErrorDto("404", ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handeException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        List<FieldErrorDto> errors = null;
        if (ex.getBindingResult().hasFieldErrors()) {
            errors = ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                    .toList();
        }

        return new ModelAndView("error",
                "error", new ErrorDto("400", "Validation error", errors));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handeException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ModelAndView("error",
                "error", new ErrorDto("500", "Unexpectable error", null));
    }
}
