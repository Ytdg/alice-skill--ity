package com.hackaton.alicecity.common;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;


public enum CommandType {
    INIT_GAME(""),
    START_GAME("играть"),//0<5
    HELP("помощь"),
    PROFILE("профиль"),

    SHOW_LAST_ALPHABET_CITY("буква"),
    RULE("правила"),

    YES("да"),
    NO("нет");


    @Getter
    private final String value;

    CommandType(@NonNull String value) {
        this.value = value;
    }

    @Nullable
    public static CommandType valueFromString(String commandText) {
        Objects.requireNonNull(commandText);
        for (CommandType command: CommandType.values()) {
            if(command.value.equals(commandText)){
                return  command;
            }
        }
        return null;
    }

   /* @Getter
    private final int value;

    CommandType(int value) {
        this.value = value;
    }

    //возврозает -1, если такой комбинации нет, иначе сумму комманд
    public int plus(CommandType command) {
        if (this.value - command.value == 5) {
            return this.value + command.value;
        }
        return -1;
    }

    public boolean verify(CommandType command) {

    }*/
}
