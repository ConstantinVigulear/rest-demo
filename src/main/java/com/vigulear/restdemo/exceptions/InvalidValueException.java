package com.vigulear.restdemo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author : crme059
 * @created : 07-Dec-23, Thursday
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid value")
public class InvalidValueException extends Exception {
    public InvalidValueException() {
    }

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidValueException(Throwable cause) {
        super(cause);
    }
}
