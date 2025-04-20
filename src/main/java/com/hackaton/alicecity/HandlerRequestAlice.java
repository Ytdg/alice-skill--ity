package com.hackaton.alicecity;

import com.hackaton.alicecity.common.*;
import com.hackaton.alicecity.common.exception.ExceptionApp;
import com.hackaton.alicecity.common.exception.InvalidCommandException;
import com.hackaton.alicecity.common.exception.InvalidStateSessionException;
import com.hackaton.alicecity.common.exception.NotFoundCityException;
import com.hackaton.alicecity.dto.*;
import com.hackaton.alicecity.entity.Profile;
import com.hackaton.alicecity.entity.SessionEntity;
import com.hackaton.alicecity.entity.User;
import com.hackaton.alicecity.service.CityService;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
class InterceptorRequestAlice extends HandlerRequestAbstract {
    @Getter
    @Setter
    private Request request;

    @Getter
    @Setter
    private SessionEntity session;

    @Getter
    private final MessageFromResource messageSource;

    @Getter
    private final CityService service;

    InterceptorRequestAlice(MessageFromResource message, CityService service) {
        this.messageSource = message;
        this.service = service;
    }

    //очищаем кэш после обработки запроса
    private void clearCash() {
        request = null;
        session = null;
    }

    interface ExecuteCommand {
        Response execute() throws ExceptionApp;
    }


    private String getProfileMessage() throws ExceptionApp {
        Profile profile = service.getProfile(new User(session.getUserId()));
        return messageSource.profileCommand(new String[]{
                String.valueOf(profile.getCountBox()),
                String.valueOf(profile.getCountStar())
        });
    }

    //перехватываюем базовые комманды
    private AliceResponse executeBaseCommand(CommandType commandType, ExecuteCommand executeCommand) throws ExceptionApp {
        Objects.requireNonNull(session);
        return switch (commandType) {
            case HELP -> UiResponseAlice.commandHelp(messageSource.allCommands());
            case RULE -> UiResponseAlice.rulesCommand(messageSource.commandRule());
            case PROFILE -> UiResponseAlice.profileCommand(getProfileMessage());
            default -> new AliceResponse(executeCommand.execute());
        };
    }

    AliceResponse executeCommand(ExecuteCommand executeCommand) throws ExceptionApp {
        log.info(request.getCommand());
        Objects.requireNonNull(request);
        CommandType commandType = CommandType.valueFromString(request.getCommand());
        AliceResponse aliceResponse;

        if (Objects.isNull(commandType)) {
            aliceResponse = new AliceResponse(executeCommand.execute());
            clearCash();
            return aliceResponse;
        }
        // обработка базовых комманд
        aliceResponse = executeBaseCommand(commandType, executeCommand);
        clearCash();
        return aliceResponse;
    }
}

@Component
@Slf4j
public class HandlerRequestAlice extends InterceptorRequestAlice {

    HandlerRequestAlice(MessageFromResource messageSource, CityService cityService) {
        super(messageSource, cityService);
    }


    @Nullable
    private AliceResponse requireButtonPressed() {
        if (getRequest().getTypeRequest().equals(TypeRequest.ButtonPressed)) {
            return new AliceResponse(new Response(getMessageSource().sorryFunctionNotFeasible()));
        }
        return null;
    }

    public AliceResponse handle(AliceRequest aliceRequest) throws ExceptionApp {
        super.handleWithBaseValidation(aliceRequest);

        log.info("id_session:" + aliceRequest.getSession().getSessionId());
        Session session = aliceRequest.getSession();
        setRequest(aliceRequest.getRequest());

        //все вводится через TypeRequest.SimpleUtterance.    TypeRequest.ButtonPressed для ссылок
        AliceResponse response = requireButtonPressed();
        if (Objects.nonNull(response)) {
            return response;
        }
        //получаем текущию сессию
        SessionEntity sessionEntity = getService().getSessionWithUpdateExpire(new SessionEntity(session.getSessionId()));
        if (Objects.isNull(sessionEntity)) {
            sessionEntity = new SessionEntity(session.getSessionId(), StateSession.START_APPLICATION, session.getUser().getUserId());
            getService().createNewSession(sessionEntity);
        }
        setSession(sessionEntity);
        return handleState(sessionEntity.getState());
    }

    private AliceResponse handleState(StateSession stateSession) throws ExceptionApp {
        switch (stateSession) {
            case START_APPLICATION -> {
                return executeCommand(this::startApplicationState);
            }
            case MOVE_TO_CITY -> {
                return executeCommand(this::startGameState);
            }
            case FOUND_CITY -> {
                return executeCommand(this::foundCityState);
            }
            case CAPITAN_GAV -> {
                return executeCommand(this::capitanGavState);
            }
        }
        throw new InvalidStateSessionException("incorrect state session", null);
    }

    private void updateStateSession(StateSession stateSession) throws ExceptionApp {
        getSession().setState(stateSession);
        getService().sessionUpdateState(getSession());
    }

    //https://humanly-intimate-rockling.cloudpub.ru/
    private Response foundCityState() throws ExceptionApp {
        CommandType commandType = CommandType.valueFromString(getRequest().getCommand());
        if (Objects.nonNull(commandType)) {
            switch (commandType) {

                case YES -> {
                    StateSession stateSession = getService().selectStateSession(getSession());
                    switch (stateSession) {
                        case OPEN_BOX -> {
                            Profile profile = getService().getProfile(new User(getSession().getUserId()));
                            return UiResponseAlice.meetPerson(getMessageSource().meetPerson(new String[]{String.valueOf(profile.getCountStar())}));
                        }
                        case NOTHING_CITY -> {
                            return UiResponseAlice.nothingCity(getMessageSource().nothingCity());
                        }
                        case FELL_OUT_BOX -> {
                            return UiResponseAlice.openBox(getMessageSource().fallBox());
                        }
                        case CAPITAN_GAV -> {
                            Result query = getService().getQuery(getSession());
                            return UiResponseAlice.capitanGav(getMessageSource().capitanGav(new String[]{query.getQuestion()}));
                        }
                    }
                }
                case NO -> {
                    updateStateSession(StateSession.MOVE_TO_CITY);
                    return UiResponseAlice.continueCommand(getMessageSource().continueFoundCity());
                }
            }
        }
        throw new InvalidCommandException("incorrect command", null);
    }

    //команд нет-название города
    private Response startGameState() throws ExceptionApp {
        CommandType commandType = CommandType.valueFromString(getRequest().getCommand());
        if (Objects.nonNull(commandType)) {
            switch (commandType) {
                case SHOW_LAST_ALPHABET_CITY -> {
                    Profile profile = getService().getProfile(new User(getSession().getUserId()));
                    return UiResponseAlice.showAlphabetLastCityCommand(getMessageSource().showLastAlphabetCityCommand(
                            new String[]{
                                    Objects.isNull(profile.getLastCityChar()) ? "любая буква" : profile.getLastCityChar()
                            }
                    ));
                }
                default -> throw new NotFoundCityException("city not found", null);
            }
        }
        try {
            getService().saveCity(getSession(), getRequest().getNlu().getEntities().stream().findFirst().orElseThrow(), getRequest().getCommand());
            updateStateSession(StateSession.FOUND_CITY);
            return UiResponseAlice.foundCityCommand(getMessageSource().cityFoundCommand());//test
        } catch (NoSuchElementException ex) {
            throw new NotFoundCityException("city not found", null);
        }
    }


    private interface ConsumerWithException {
        Response accept(boolean value) throws ExceptionApp;
    }

    private Response capitanGavState() throws ExceptionApp {
        CommandType commandType = CommandType.valueFromString(getRequest().getCommand());
        ConsumerWithException consumer = v -> {
            updateStateSession(StateSession.MOVE_TO_CITY);
            if (getService().verifyCorrectAnswer(false, getSession())) {
                Profile profile = getService().getProfile(new User(getSession().getUserId()));
                return UiResponseAlice.correctAnswer(getMessageSource().correctAnswer(new String[]{String.valueOf(profile.getCountBox())}));
            }

            return UiResponseAlice.inCorrectAnswer(getMessageSource().inCorrectAnswer());
        };
        if (Objects.nonNull(commandType)) {
            switch (commandType) {
                case NO -> {
                    return consumer.accept(false);
                }
                case YES -> {
                    return consumer.accept(true);
                }
            }
        }
        throw new InvalidCommandException("invalid command", null); // неверно
    }

    //начать,инилиализация
    private Response startApplicationState() throws ExceptionApp {

        CommandType commandType = CommandType.valueFromString(getRequest().getCommand());
        if (!Objects.isNull(commandType)) {
            switch (commandType) {
                case INIT_GAME -> {
                    return UiResponseAlice.initCommand(getMessageSource().getGreetingStartFirstTime());
                }
                case START_GAME -> {
                    updateStateSession(StateSession.MOVE_TO_CITY);
                    return UiResponseAlice.stateStartGameCommand(getMessageSource().startGameCommand());
                }
            }
        }
        throw new InvalidCommandException("incorrect command", null);
    }
}
