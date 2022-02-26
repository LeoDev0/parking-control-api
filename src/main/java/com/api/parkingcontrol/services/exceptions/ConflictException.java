package com.api.parkingcontrol.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    private static String MESSAGE = "Conflict.";

    public ConflictException() {
        super(MESSAGE);
    }

    public ConflictException(String customMessage) {
        super(customMessage);
    }
}
