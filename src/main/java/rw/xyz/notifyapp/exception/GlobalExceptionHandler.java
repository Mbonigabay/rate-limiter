package rw.xyz.notifyapp.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends Throwable {
    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<?> tooManyRequestException(TooManyRequestException ex) {
      HttpStatus conflict = HttpStatus.TOO_MANY_REQUESTS;
      ErrorDetails errorDetails = new ErrorDetails(ZonedDateTime.now(ZoneId.of("Z")), ex.getMessage(), ex,
          HttpStatus.TOO_MANY_REQUESTS);
      return new ResponseEntity<>(errorDetails, conflict);
    }
}
