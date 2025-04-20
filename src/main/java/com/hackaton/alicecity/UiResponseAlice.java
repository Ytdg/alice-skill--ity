package com.hackaton.alicecity;

import com.hackaton.alicecity.common.CommandType;
import com.hackaton.alicecity.dto.AliceResponse;
import com.hackaton.alicecity.dto.Button;
import com.hackaton.alicecity.dto.Response;

import java.util.List;

//требуется рефакторинг
public class UiResponseAlice {
    private UiResponseAlice() {
    }


    public static AliceResponse commandHelp(String text) {
        Response response = new Response(text);
        response.setButtons(List.of(
                new Button(false, CommandType.RULE.getValue()),
                new Button(false, CommandType.PROFILE.getValue())
        ));
        return new AliceResponse(response);
    }

    //специфичные комманды возвращают Response
    public static Response initCommand(String text) {
        Response response = new Response(text, false);
        response.setButtons(
                List.of(new Button(true, CommandType.START_GAME.getValue()),
                        new Button(true, CommandType.HELP.getValue())
                )
        );
        return response;
    }

    public static AliceResponse rulesCommand(String text) {
        Response response = new Response(text, false);
        response.setButtons(
                List.of(new Button(true, CommandType.START_GAME.getValue())
                )
        );
        return new AliceResponse(response);
    }


    public static AliceResponse profileCommand(String text) {
        return new AliceResponse(new Response(text, false));
    }

    public static Response stateStartGameCommand(String text) {
        return new Response(text, false);
    }

    public static Response showAlphabetLastCityCommand(String text) {
        return new Response(text, false);
    }

    public static Response foundCityCommand(String text) {
        Response response = new Response(text);
        response.setButtons(List.of(new Button(true, CommandType.NO.getValue()), new Button(true, CommandType.YES.getValue())));
        return response;
    }

    public static Response continueCommand(String text) {
        return new Response(text);
    }


    public static Response capitanGav(String text) {
        Response response = new Response(text);
        response.setButtons(List.of(
                new Button(true, CommandType.NO.getValue()),
                new Button(true, CommandType.YES.getValue())
        ));
        return response;
    }

    public static Response openBox(String text) {
        return new Response(text);
    }

    public static Response meetPerson(String text) {
        return new Response(text);
    }

    public static Response nothingCity(String text) {
        return new Response(text);
    }

    public static Response correctAnswer(String text) {
        return new Response(text);
    }

    public static Response inCorrectAnswer(String text) {
        return new Response(text);
    }

}

