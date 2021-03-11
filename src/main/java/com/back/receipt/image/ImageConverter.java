package com.back.receipt.image;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ImageConverter {

/*    public String encodeToString(final File file) throws IOException {
        String encodedfile;

        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        encodedfile = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);

        return encodedfile;
    }*/

    public String encodeToString(final InputStream file) throws IOException {

        byte[] imageBytes = new byte[file.available()];
        file.read(imageBytes, 0, imageBytes.length);
        file.close();
        String imageStr = Base64.encodeBase64String(imageBytes);

        return imageStr;
    }

}