package com.vigulear.restdemo.controller;

import com.vigulear.restdemo.exceptions.InvalidValueException;
import com.vigulear.restdemo.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : crme059
 * @created : 01-Dec-23, Friday
 */
@ControllerAdvice
public class CustomErrorController {
  private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidValueException(InvalidValueException exception) {
      log.error("Bad request error: {}", exception.toString());
      return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException exception) {
      log.error("Bad request error: {}", exception.toString());
      return new ResponseEntity<>(
          "Invalid value \"" + exception.getValue() + "\" for parameter", HttpStatus.BAD_REQUEST);
    }

  @ExceptionHandler
  public ResponseEntity<List<Map<String, String>>> handleJPAViolation(
      TransactionSystemException exception) {
    ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

    if (exception.getCause().getCause() instanceof ConstraintViolationException) {
      ConstraintViolationException ve =
          (ConstraintViolationException) exception.getCause().getCause();

      List<Map<String, String>> errors =
          ve.getConstraintViolations().stream()
              .map(
                  constraintViolation -> {
                    Map<String, String> errMap = new HashMap<>();
                    errMap.put(
                        constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage());
                    return errMap;
                  })
              .toList();
      return responseEntity.body(errors);
    }

    return responseEntity.build();
  }

  @ExceptionHandler
  public ResponseEntity<String> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    log.error("Bad request error: {}", exception.toString());
    return new ResponseEntity<>("Unrecognized token for parameter", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
    log.error("Bad request error: {}", exception.toString());
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<Map<String, String>>> handleBindException(
      MethodArgumentNotValidException exception) {

    List<Map<String, String>> errorList =
        exception.getFieldErrors().stream()
            .map(
                fieldError -> {
                  Map<String, String> errorMap = new HashMap<>();
                  errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                  return errorMap;
                })
            .toList();

    return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
  }
}
