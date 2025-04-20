package com.hackaton.alicecity.common.exception;

import com.hackaton.alicecity.common.exception.ExceptionApp;

public class InvalidCommandException extends ExceptionApp {

    public InvalidCommandException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
