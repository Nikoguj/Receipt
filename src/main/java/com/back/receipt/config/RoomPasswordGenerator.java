package com.back.receipt.config;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RoomPasswordGenerator {

    public String generate() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toUpperCase();

    }
}