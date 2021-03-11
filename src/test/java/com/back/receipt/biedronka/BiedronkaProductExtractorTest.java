package com.back.receipt.biedronka;

import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.image.ImageConverter;
import com.back.receipt.domain.Product;
import com.back.receipt.google.GoogleClient;
import com.back.receipt.google.domain.GoogleResponse;
import com.back.receipt.google.mapper.GoogleMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BiedronkaProductExtractorTest {

    @Autowired
    private GoogleClient googleClient;

    @Autowired
    private GoogleMapper googleMapper;

    @Autowired
    private BiedronkaProductExtractor biedronkaProductExtractor;

    @Autowired
    private ImageConverter imageConverter;

    String exampleImagePath;

    @BeforeEach
    public void initialize() {
        exampleImagePath = "exampleImage/paragBiedro.jpeg";
    }

    @Test
        public void extract() throws IOException, MyResourceNotFoundException {
        //Given
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(exampleImagePath).getFile());
        InputStream inputStream = new FileInputStream(file);
        GoogleResponse googleResponse = googleMapper.mapToGoogleResponse(googleClient.getGoogleDto(imageConverter.encodeToString(inputStream)));

        Product product1 = new Product("Jaja kl M x10szt C", 3.0, 3.99, 0, 0, 11.97);
        Product product2 = new Product("Cukier Biały 1kg B", 1.0, 2.49, 0, 0, 2.49);
        Product product3 = new Product("Chleb kr 500g", 1.0, 1.59, 0.0, 0.0, 1.59);
        Product product4 = new Product("JajkoNiespodzLE 20g A", 1.0, 3.69, 0.0, 0.0, 3.69);
        Product product5 = new Product("Mleko UHT 1,5 11 C", 1.0, 1.95, 0.0, 0.0, 1.95);
        Product product6 = new Product("Ser Swiatowid 500g C", 1.0, 9.69, 0.0, 0.0, 9.69);
        Product product7 = new Product("Olej Wyborny 11", 1.0, 4.69, 0.0, 0.0, 4.69);
        Product product8 = new Product("Marg RAMA Poje 1kg", 1.0, 8.99, 8.99, 4.0, 4.99);
        Product product9 = new Product("Sok Pom Riviva 21 C", 1.0, 5.35, 0.0, 0.0, 5.35);

        List<Product> exceptedProductList = new ArrayList<>(Arrays.asList(
                product1,
                product2,
                product3,
                product4,
                product5,
                product6,
                product7,
                product8,
                product9
        ));

        //When
        List<Product> returnProductList = biedronkaProductExtractor.extract(googleResponse);

        System.out.println(returnProductList);

        //Then
        Assert.assertEquals(exceptedProductList.size(), returnProductList.size());
        for (int i = 0; i < exceptedProductList.size(); i++) {
            Assert.assertEquals(exceptedProductList.get(i), returnProductList.get(i));
        }
        Assert.assertEquals(exceptedProductList, returnProductList);

    }

}