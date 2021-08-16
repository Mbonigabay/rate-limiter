package rw.xyz.notifyapp.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class ErrorDetails {
    private final ZonedDateTime timestamp;
    private final String message;
//    private final Throwable details;
    private  final HttpStatus httpStatus;


    public ErrorDetails(ZonedDateTime timestamp,
                        String message,
                        Throwable details,
                        HttpStatus httpStatus
                        ) {
        super();
        this.timestamp = timestamp;
        this.message = message;
//        this.details = details;
        this.httpStatus = httpStatus;

    }
        public ZonedDateTime getTimestamp(){return timestamp; }
        public String getMessage(){return message;}
//        public  Throwable getDetails(){return details;}
        public  HttpStatus getHttpStatus(){return httpStatus;}
}