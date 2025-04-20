package com.hackaton.alicecity.common;

public enum StateSession {
    START_APPLICATION,
    MOVE_TO_CITY,
    FOUND_CITY,
    CAPITAN_GAV,

    FELL_OUT_BOX,

    NOTHING_CITY,

    OPEN_BOX;

    public static final StateSession[] possibleStateBeforeFoundCity = {CAPITAN_GAV, FELL_OUT_BOX, NOTHING_CITY, OPEN_BOX};
}
