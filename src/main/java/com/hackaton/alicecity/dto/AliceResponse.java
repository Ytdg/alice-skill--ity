package com.hackaton.alicecity.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.PackagePrivate;

@Data
@RequiredArgsConstructor
public class AliceResponse {
    @NonNull
    private Response response;

    private final String version = "1.0";


}
