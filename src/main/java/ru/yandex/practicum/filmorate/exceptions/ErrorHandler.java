package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public ResponseEntity<Map<String, String>> handleDeletionException(final DeletionException e) {
        return new ResponseEntity<>(Map.of("User", e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleSameIdException(final SameIdException e) {
        return new ResponseEntity<>(Map.of("User", e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleEmptyResult(final EmptyResultDataAccessException e) {
        return new ResponseEntity<>(Map.of("User/Film", "Фильм или Пользователь не найден"), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleLikeResult(final LikeException e) {
        return new ResponseEntity<>(Map.of("User/Film", "Фильм или Пользователь не найден"), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleDataAccessResult(final DataAccessException e) {
        return new ResponseEntity<>(Map.of("User/Film", "Фильм или Пользователь не найден"), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleEmptyGenreResult(final GenreNotFoundException e) {
        return new ResponseEntity<>(Map.of("User/Film", e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleMpaResult(final MpaNotFoundException e) {
        return new ResponseEntity<>(Map.of("User/Film", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleOtherThroable(Throwable e) {
        return new ResponseEntity<>(Map.of(e.getClass().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
