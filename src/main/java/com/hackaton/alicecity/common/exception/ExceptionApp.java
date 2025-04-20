package com.hackaton.alicecity.common.exception;

import lombok.Getter;

public class ExceptionApp extends Throwable {
    @Getter
    private final String message;
    @Getter
    private final Throwable throwable;

    protected ExceptionApp(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

}
