package com.hackaton.alicecity.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class Button {
    @NonNull
    private String title;
    private boolean hide;

    public Button(boolean hide, @NonNull String title) {
        this.hide = hide;
        this.title = title;
    }
}
