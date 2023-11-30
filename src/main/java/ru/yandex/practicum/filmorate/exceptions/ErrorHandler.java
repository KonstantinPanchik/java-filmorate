package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleHappinessOverflow(final UserNotFoundException e) {
        ResponseEntity<Map<String, String>> user = new ResponseEntity<>(
                Map.of("User", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
        return user;
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleFilmNotFound(final FilmNotFoundException e) {
        return new ResponseEntity<>(Map.of("Film", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleOtherThroable(Throwable e) {
        return new ResponseEntity<>(Map.of(e.getClass().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
