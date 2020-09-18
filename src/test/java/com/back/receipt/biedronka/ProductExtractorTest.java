package com.back.receipt.biedronka;

import com.back.receipt.converter.ImageConverter;
import com.back.receipt.google.GoogleClient;
import com.back.receipt.google.domain.GoogleResponse;
import com.back.receipt.google.mapper.GoogleMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductExtractorTest {

    @Autowired
    private GoogleClient googleClient;

    @Autowired
    private GoogleMapper googleMapper;

    @Autowired
    private ProductExtractor productExtractor;

    @Autowired
    private ImageConverter imageConverter;

    private static String exampleImagePath = "exampleImage/paragon.jpg";

    @Test
    public void extract() throws IOException {
        //Given
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(exampleImagePath).getFile());
        GoogleResponse googleResponse = googleMapper.mapToGoogleResponse(googleClient.getGoogleDto(imageConverter.encodeToString(file)));
        productExtractor.extract(googleResponse);

    }
}