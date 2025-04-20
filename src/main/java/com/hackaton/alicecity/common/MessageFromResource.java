package com.hackaton.alicecity.common;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageFromResource {
    private final MessageSource messageSource;
    private final Object[] param = new Object[]{};
    private final Locale locale = new Locale("ru");

    MessageFromResource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    //получаем текст из properties
    public String getGreetingStartFirstTime() {
        return messageSource.getMessage("greeting.message.start-first-time", param, locale);
    }

    public String sorryFunctionNotFeasible() {
        return messageSource.getMessage("sorry.function.not_feasible", param, locale);
    }

    public String allCommands() {
        return messageSource.getMessage("command.message", param, locale);
    }

    public String commandRule() {
        return messageSource.getMessage("command.rules", param, locale);
    }

    public String startGameCommand() {
        return messageSource.getMessage("start.game", param, locale);
    }

    public String profileCommand(String[] param) {
        return messageSource.getMessage("profile", param, locale);
    }

    public String showLastAlphabetCityCommand(String[] param) {
        return messageSource.getMessage("last.alpha.city", param, locale);
    }

    public String cityFoundCommand() {
        return messageSource.getMessage("city.found", param, locale);
    }

    public String continueFoundCity() {
        return messageSource.getMessage("continue", param, locale);
    }

    public String capitanGav(String[] param) {
        return messageSource.getMessage("capitan.gav", param, locale);
    }

    public String meetPerson(String[] param) {
        return messageSource.getMessage("meet.person", param, locale);
    }

    public String fallBox() {
        return messageSource.getMessage("fall.box", param, locale);
    }

    public String nothingCity() {
        return messageSource.getMessage("nothing.city", param, locale);
    }

    public String correctAnswer(String[] param) {
        return messageSource.getMessage("answer.correct", param, locale);
    }

    public String inCorrectAnswer() {
        return messageSource.getMessage("answer.incorrect", param, locale);
    }
}
