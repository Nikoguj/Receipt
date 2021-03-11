package com.back.receipt.text;

import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.google.domain.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FinderTest {

    @Autowired
    private Finder finder;

    @Test
    public void getBoundingPolyWord() throws MyResourceNotFoundException {
        //Given
        GoogleResponse googleResponse = new GoogleResponse();
        GoogleResponses googleResponses = new GoogleResponses();

        for (int i = 0; i < 5; i++) {
            GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly();
            for(int j = 0; j < 4; j++) {
                googleBoundingPoly.getVertices().add(new GoogleVertex(i*100+j, i*100+j));
            }
            googleResponses.getTextAnnotations().add(new GoogleTextAnnotation("pl", String.valueOf(i), googleBoundingPoly));
        }

        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly();
        googleBoundingPoly.getVertices().add(new GoogleVertex(50, 50));
        googleBoundingPoly.getVertices().add(new GoogleVertex(51, 51));
        googleBoundingPoly.getVertices().add(new GoogleVertex(52, 52));
        googleBoundingPoly.getVertices().add(new GoogleVertex(53, 53));
        GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation("pl" , "WORD", googleBoundingPoly);
        googleResponses.getTextAnnotations().add(googleTextAnnotation);

        googleResponse.getGoogleResponsesList().add(googleResponses);

        //When
        GoogleBoundingPoly returnGoogleBoundingPoly = finder.getBoundingPolyWord(googleResponse, "WORD");

        //Then
        Assert.assertEquals(googleBoundingPoly, returnGoogleBoundingPoly);
        Assert.assertEquals(new GoogleVertex(50, 50), returnGoogleBoundingPoly.getVertices().get(0));
        Assert.assertEquals(new GoogleVertex(51, 51), returnGoogleBoundingPoly.getVertices().get(1));
        Assert.assertEquals(new GoogleVertex(52, 52), returnGoogleBoundingPoly.getVertices().get(2));
        Assert.assertEquals(new GoogleVertex(53, 53), returnGoogleBoundingPoly.getVertices().get(3));

    }
}