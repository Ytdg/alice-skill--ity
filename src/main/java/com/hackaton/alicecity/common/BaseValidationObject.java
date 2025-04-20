package com.hackaton.alicecity.common;

import com.hackaton.alicecity.common.exception.ExceptionApp;

public interface BaseValidationObject {
   void requireValidate() throws ExceptionApp;
}
