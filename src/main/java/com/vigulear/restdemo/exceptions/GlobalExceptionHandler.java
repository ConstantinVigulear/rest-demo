package com.vigulear.restdemo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author : crme059
 * @created : 01-Dec-23, Friday
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler
  public ResponseEntity<String> handleException(Exception exception) {
    log.error("Bad request error: {}", exception.toString());
    exception.printStackTrace();
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<String> handleNotFoundException(NoContentException exception) {
    log.error("Bad request error: {}", exception.toString());
    exception.printStackTrace();
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NO_CONTENT);
  }

  @ExceptionHandler
  public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException exception) {
    log.error("Bad request error: {}", exception.toString());
    exception.printStackTrace();
    return new ResponseEntity<>(
        "Invalid value \"" + exception.getValue() + "\" for parameter", HttpStatus.BAD_REQUEST);
  }
}
