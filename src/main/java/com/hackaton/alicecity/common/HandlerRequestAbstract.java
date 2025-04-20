package com.hackaton.alicecity.common;

import com.hackaton.alicecity.common.exception.ExceptionApp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HandlerRequestAbstract {
    protected void handleWithBaseValidation(BaseValidationObject object) throws ExceptionApp {
        log.info("test");
        object.requireValidate();
    }
}
