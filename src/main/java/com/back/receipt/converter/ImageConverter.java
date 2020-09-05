package com.back.receipt.converter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class ImageConverter {

    public String encodeToString(File file) throws IOException {
        String encodedfile;

        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        encodedfile = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);

        return encodedfile;
    }
}