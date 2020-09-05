package com.back.receipt.converter;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageConverterTest {

    @Autowired
    private ImageConverter imageConverter;

    private static String imageBase64 = "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";
    private static String exampleImagePath = "exampleImage/hi.jpg";

    @Test
    public void encodeToString() throws IOException {
        //Given
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(exampleImagePath).getFile());

        //When
        String returnBase64 = imageConverter.encodeToString(file);

        //Then
        Assert.assertEquals(imageBase64, returnBase64);
    }
}