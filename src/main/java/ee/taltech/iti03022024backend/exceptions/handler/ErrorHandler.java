package ee.taltech.iti03022024backend.exceptions.handler;

import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.UnfilledFieldException;
import ee.taltech.iti03022024backend.exceptions.response.ExceptionResponse;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    /**
     * Handles uncaught exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        log.error("Internal server error", ex);
        return new ResponseEntity<>(new ExceptionResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles 404 (not found) type exceptions.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions caused by incorrect requests (empty fields, incorrect data etc.).
     */
    @ExceptionHandler(UnfilledFieldException.class)
    public ResponseEntity<ExceptionResponse> handleUnfilledFieldException(Exception ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NameAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleNameAlreadyExistsException(Exception ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
