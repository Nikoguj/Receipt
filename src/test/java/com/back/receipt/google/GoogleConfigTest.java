package com.back.receipt.google;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class GoogleConfigTest {

    @Autowired
    private GoogleConfig googleConfig;

    @Test
    void getGoogleAppEndpoint() {
        //When
        String returnString = googleConfig.getGoogleApiEndpoint();

        //Then
        Assert.assertEquals("https://vision.googleapis.com/v1/images:annotate", returnString);
    }
}