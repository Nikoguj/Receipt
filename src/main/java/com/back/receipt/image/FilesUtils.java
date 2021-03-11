package com.back.receipt.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FilesUtils {

    public static File convert(MultipartFile file) throws IOException {
        File fileToSave = File.createTempFile("image",".jpg");
        FileOutputStream fos = new FileOutputStream(fileToSave);
        fos.write(file.getBytes());
        fos.close();
        return fileToSave;
    }
}