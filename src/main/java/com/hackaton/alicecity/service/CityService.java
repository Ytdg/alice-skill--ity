package com.hackaton.alicecity.service;

import com.hackaton.alicecity.common.StateSession;
import com.hackaton.alicecity.common.exception.*;
import com.hackaton.alicecity.dto.EntityAlice;
import com.hackaton.alicecity.dto.Query;
import com.hackaton.alicecity.dto.Result;
import com.hackaton.alicecity.entity.Cities;
import com.hackaton.alicecity.entity.Profile;
import com.hackaton.alicecity.entity.SessionEntity;
import com.hackaton.alicecity.entity.User;
import com.hackaton.alicecity.repository.*;
import com.hackaton.alicecity.util.JsonCoder;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

@Service
@Slf4j
public class CityService {
    private final SessionRepository sessionRepository;
    private final UsersRepository usersRepository;
    private final ProfileRepository profileRepository;
    private final CitiesRepository citiesRepository;
    private final QuizRepository quizRepository;

    public CityService(SessionRepository sessionRepository, UsersRepository usersRepository, ProfileRepository repository,
                       CitiesRepository citiesRepository, QuizRepository quizRepository) {
        this.sessionRepository = sessionRepository;
        this.usersRepository = usersRepository;
        this.profileRepository = repository;
        this.citiesRepository = citiesRepository;
        this.quizRepository = quizRepository;
    }

    //session - 60 min
    private LocalDateTime createExpireTime() {
        return LocalDateTime.now().plusMinutes(60);
    }

    //доработка
    @Nullable
    @Transactional
    public SessionEntity getSessionWithUpdateExpire(SessionEntity session) throws ExceptionApp {
        try {
            SessionEntity sessionEntity = getSession(session);
            if (Objects.nonNull(sessionEntity)) {
                sessionEntity.setExpire(createExpireTime());
                sessionEntity = sessionRepository.save(sessionEntity);
            }
            return sessionEntity;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new NotGetObjectException("couldn't get object", ex);
        }
    }

    @Transactional
    public void sessionUpdateState(SessionEntity session) throws ExceptionApp {
        try {

            SessionEntity sessionEntity = getSession(session);
            Objects.requireNonNull(sessionEntity);
            sessionEntity.setState(session.getState());
            sessionEntity.setPayload(session.getPayload());
            sessionRepository.save(sessionEntity);
        } catch (Exception ex) {
            throw new NotGetObjectException("couldn't get object", ex);
        }
    }

    @SneakyThrows
    private StateSession generateStateSession(int startIndex, int endIndex, Profile profile, SessionEntity session) {
        StateSession[] stateSessions = StateSession.possibleStateBeforeFoundCity;
        int randIndex = new Random().nextInt(startIndex, endIndex);

        return switch (stateSessions[randIndex]) {
            case CAPITAN_GAV -> {
                try {
                    Result query = quizRepository.getQuestion();
                    Objects.requireNonNull(query.getQuestion());
                    if (!query.getQuestion().isBlank()) {
                        session.setPayload(JsonCoder.encode(query));
                        session.setState(StateSession.CAPITAN_GAV);
                        sessionUpdateState(session);
                        yield StateSession.CAPITAN_GAV;
                    }
                    throw new NotGetObjectException("could not get object", null);
                } catch (Exception ex) {
                    yield generateStateSession(startIndex + 1, endIndex, profile, session);
                }
            }
            case NOTHING_CITY -> {
                session.setState(StateSession.MOVE_TO_CITY);
                sessionUpdateState(session);
                yield StateSession.NOTHING_CITY;
            }
            case FELL_OUT_BOX -> {
                session.setState(StateSession.MOVE_TO_CITY);
                sessionUpdateState(session);
                yield StateSession.FELL_OUT_BOX;
            }
            case OPEN_BOX -> {
                if (profile.getCountBox() > 0) {
                    session.setState(StateSession.MOVE_TO_CITY);
                    sessionUpdateState(session);
                    yield StateSession.OPEN_BOX;
                }
                yield generateStateSession(startIndex, endIndex - 1, profile, session);
            }
            default -> throw new InvalidStateSessionException("invalid state", null);
        };

    }

    @Nullable
    private SessionEntity getSession(SessionEntity session) throws ExceptionApp {
        Optional<SessionEntity> optionalSessionEntity = sessionRepository.findById(session.getSessionId());
        return optionalSessionEntity.orElse(null);
    }

    public Result getQuery(SessionEntity session) throws ExceptionApp {
        try {
            SessionEntity sessionEntity = getSession(session);
            Objects.requireNonNull(sessionEntity);
            return JsonCoder.decode(Result.class, sessionEntity.getPayload());
        } catch (Exception ex) {
            throw new NotGetObjectException("could not object", ex);
        }
    }

    private void profileSave(SessionEntity session, Consumer<Profile> consumer) {
        Profile profile = profileRepository.findById(session.getUserId()).orElse(null);
        Objects.requireNonNull(profile);
        consumer.accept(profile);
        profileRepository.save(profile);
    }

    @Transactional
    public StateSession selectStateSession(SessionEntity session) throws ExceptionApp {

        StateSession stateSession = generateStateSession(0,
                StateSession.possibleStateBeforeFoundCity.length, getProfile(new User(session.getUserId())), session);
        return switch (stateSession) {
            case CAPITAN_GAV -> StateSession.CAPITAN_GAV;
            case OPEN_BOX -> {
                int countStar = new Random().nextInt(1, 5);
                profileSave(session, profile -> {
                    profile.setCountStar(profile.getCountStar() + countStar);
                    profile.setCountBox(profile.getCountBox() - 1);
                });
                yield StateSession.OPEN_BOX;
            }
            case NOTHING_CITY -> StateSession.NOTHING_CITY;
            case FELL_OUT_BOX -> {
                profileSave(session, profile -> {
                    profile.setCountBox(profile.getCountBox() + 1);
                });
                yield StateSession.FELL_OUT_BOX;
            }
            default -> throw new InvalidStateSessionException("invalid state", null);
        };

    }

    private void saveUserTalkCity(String userCityTalk, Profile profile) throws ExceptionApp {
        log.info(userCityTalk);
        boolean existsCity = citiesRepository.existsByCityNameAndUserId(userCityTalk, profile.getUserId());
        log.info("city_user_talk:" + userCityTalk + "/fact:" + existsCity);
        Runnable save = () -> {
            String last = String.valueOf(userCityTalk.charAt(userCityTalk.length() - 1));
            last = !last.equals("ь") && !last.equals("ъ") && !last.equals("ы") ? last : String.valueOf(userCityTalk.charAt(userCityTalk.length() - 2));
            profile.setLastCityChar(last);
            profileRepository.save(profile);
            citiesRepository.save(new Cities(userCityTalk, profile.getUserId()));
        };
        if (Objects.isNull(profile.getLastCityChar()) && !existsCity) {
            save.run();
            return;
        }
        Objects.requireNonNull(profile.getLastCityChar());
        if (profile.getLastCityChar().equals(String.valueOf(userCityTalk.charAt(0))) && !existsCity) {
            save.run();
            return;
        }
        if (existsCity) {
            throw new RepeatCityException("not get city", null);
        }
        throw new AccessDeniedCityException("not fount city on map", null);

    }

    @Transactional
    public boolean verifyCorrectAnswer(boolean userAnswer, SessionEntity session) throws ExceptionApp {
        try {
            SessionEntity sessionEntity = getSession(session);
            Objects.requireNonNull(sessionEntity);
            Objects.requireNonNull(sessionEntity.getPayload());
            Result translateQuery = JsonCoder.decode(Result.class, sessionEntity.getPayload());
            if (translateQuery.isCorrectAnswer() == userAnswer) {
                profileSave(session, profile -> {
                    profile.setCountBox(profile.getCountBox() + new Random().nextInt(1, 3));
                });
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new NotGetObjectException("could not get object", ex);
        }
    }

    @Transactional
    public void saveCity(SessionEntity session, EntityAlice entityAlice, String userCityTalk) throws ExceptionApp {
        if (Objects.nonNull(entityAlice.getValue().getCity())) {
            Profile profile = getProfile(new User(session.getUserId()));
            saveUserTalkCity(userCityTalk, profile);
            return;
        }
        throw new NotFoundCityException("not found city", null);
    }

    @Transactional
    public void createNewSession(SessionEntity session) throws ExceptionApp {
        try {
            log.info(session.getSessionId());
            if (!usersRepository.existsById(session.getUserId())) {
                usersRepository.save(new User(session.getUserId()));
                profileRepository.save(new Profile(session.getUserId(), 0, 0, null));
            }
            session.setExpire(createExpireTime());
            sessionRepository.save(session);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new NotGetObjectException("couldn't get object", ex);
        }
    }

    public Profile getProfile(User user) throws ExceptionApp {
        try {
            return profileRepository.findById(user.getUserId()).orElseThrow();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new NotGetObjectException("couldn't get object", ex);
        }
    }
}
