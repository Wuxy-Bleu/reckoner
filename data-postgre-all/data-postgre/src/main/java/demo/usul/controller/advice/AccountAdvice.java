package demo.usul.controller.advice;

import demo.usul.exception.PostgreDeleteException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.GONE;

@RestControllerAdvice
public class AccountAdvice {

    @ExceptionHandler(PostgreDeleteException.class)
    public ResponseEntity<String> handleValidationErrors(PostgreDeleteException ex) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), GONE);
    }

}
