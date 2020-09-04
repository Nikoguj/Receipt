package com.back.receipt.google;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GoogleConfig {

    @Value("${google.api.endpoint}")
    private String googleApiEndpoint;

    @Value("${google.app.key}")
    private String googleAppKey;
}