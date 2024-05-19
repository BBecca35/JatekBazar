package hu.nye.home.controller;

import hu.nye.home.exceptions.GameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice to handle exceptions globally.
 */
@SuppressWarnings("checkstyle:Indentation")
@RestControllerAdvice
public class ExceptionHandlerController {
    
    /**
     * Handles GameNotFoundException and returns HTTP 404 Not Found.
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Game not found in our database!")
    @ExceptionHandler(GameNotFoundException.class)
    public void gameNotFoundExceptionHandler() {
    
    }
}
