package com.hackaton.alicecity;

import com.hackaton.alicecity.common.CommandType;
import com.hackaton.alicecity.common.exception.*;
import com.hackaton.alicecity.dto.AliceResponse;
import com.hackaton.alicecity.dto.Button;
import com.hackaton.alicecity.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;


//исключения выбрасываються, возникшие в контроллере
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody AliceResponse handleResourceNotParseException(HttpMessageNotReadableException ex) {
        log.error(ex.getLocalizedMessage());
        return new AliceResponse(new Response("Возникла внутреняя оишбка", true));
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public @ResponseBody AliceResponse handleNotAuthorizedException(NotAuthorizedException ex) {
        log.error(ex.getLocalizedMessage());
        return new AliceResponse(new Response("Для этого навыка требуется авторизация", true));
    }

    @ExceptionHandler(InvalidCommandException.class)
    public @ResponseBody AliceResponse invalidCommandExceptionException(InvalidCommandException ex) {
        log.error(ex.getLocalizedMessage());
        return new AliceResponse(new Response("Упс..Вот тут я тебя не понял("));
    }

    @ExceptionHandler(InvalidStateSessionException.class)
    public @ResponseBody AliceResponse invalidStateSessionException(InvalidStateSessionException ex) {
        log.error(ex.getLocalizedMessage());
        return new AliceResponse(new Response("Упс.. Кажеться у меня возникли проблемы("));
    }

    @ExceptionHandler(NotFoundCityException.class)
    public @ResponseBody AliceResponse notFoundCityException(NotFoundCityException ex) {
        log.error(ex.getLocalizedMessage());
        Response response = new Response("Кажеться такого города нет( Попробуйте другой\nСкажите - буква,чтобы напомнить");
        response.setButtons(List.of(new Button(true, CommandType.SHOW_LAST_ALPHABET_CITY.getValue())));
        return new AliceResponse(response);
    }

    @ExceptionHandler(AccessDeniedCityException.class)
    public @ResponseBody AliceResponse notFoundCityException(AccessDeniedCityException ex) {
        log.error(ex.getLocalizedMessage());
        Response response = new Response("Ух.. Такого города у нас пока нет на карте\nСкажите - буква,чтобы напомнить");
        response.setButtons(List.of(new Button(true, CommandType.SHOW_LAST_ALPHABET_CITY.getValue())));
        return new AliceResponse(response);
    }
    @ExceptionHandler(RepeatCityException.class)
    public @ResponseBody AliceResponse repeatCityException(RepeatCityException ex) {
        log.error(ex.getLocalizedMessage());
        Response response = new Response("Ух.. Такой город уже был\nСкажите - буква,чтобы напомнить");
        response.setButtons(List.of(new Button(true, CommandType.SHOW_LAST_ALPHABET_CITY.getValue())));
        return new AliceResponse(response);
    }


}