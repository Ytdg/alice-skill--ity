package com.hackaton.alicecity.dto;

import com.hackaton.alicecity.common.BaseValidationObject;
import com.hackaton.alicecity.common.exception.ExceptionApp;
import com.hackaton.alicecity.common.exception.NotAuthorizedException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Objects;


@Data
@RequiredArgsConstructor
public class AliceRequest implements BaseValidationObject {
    @NonNull
    private Request request;
    @NonNull
    private Session session;


    //навык требует авторизации пользователя
    @Override
    public void requireValidate() throws ExceptionApp {
        try {
            Objects.requireNonNull(session.getUser());
        } catch (Exception ex) {
            throw new NotAuthorizedException("Требуеться авторизация", ex);
        }
    }
}
