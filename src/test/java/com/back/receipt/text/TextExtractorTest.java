package com.back.receipt.text;

import com.back.receipt.google.domain.*;
import com.back.receipt.math.LinearFunction;
import com.back.receipt.math.Quadrangle;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TextExtractorTest {

    @Test
    public void getGoogleResponsesFromQuadrangle() {
        //Given
        List<GoogleVertex> googleVertexList1 = new ArrayList<>(Arrays.asList(
                new GoogleVertex(54, 43),
                new GoogleVertex(135, 43),
                new GoogleVertex(135, 64),
                new GoogleVertex(54, 64)
        ));
        GoogleTextAnnotation googleTextAnnotation1 = new GoogleTextAnnotation("", "product1", new GoogleBoundingPoly(googleVertexList1));

        List<GoogleVertex> googleVertexList2 = new ArrayList<>(Arrays.asList(
                new GoogleVertex(6, 140),
                new GoogleVertex(89, 140),
                new GoogleVertex(89, 160),
                new GoogleVertex(6, 160)
        ));
        GoogleTextAnnotation googleTextAnnotation2 = new GoogleTextAnnotation("", "product4", new GoogleBoundingPoly(googleVertexList2));

        List<GoogleVertex> googleVertexList3 = new ArrayList<>(Arrays.asList(
                new GoogleVertex(191, 117),
                new GoogleVertex(281, 117),
                new GoogleVertex(281, 142),
                new GoogleVertex(191, 142)
        ));
        GoogleTextAnnotation googleTextAnnotation3 = new GoogleTextAnnotation("", "product2", new GoogleBoundingPoly(googleVertexList3));

        List<GoogleVertex> googleVertexList4 = new ArrayList<>(Arrays.asList(
                new GoogleVertex(101, 147),
                new GoogleVertex(184, 147),
                new GoogleVertex(184, 171),
                new GoogleVertex(101, 171)
        ));
        GoogleTextAnnotation googleTextAnnotation4 = new GoogleTextAnnotation("", "product3", new GoogleBoundingPoly(googleVertexList4));

        List<GoogleVertex> googleVertexList5 = new ArrayList<>(Arrays.asList(
                new GoogleVertex(182, 303),
                new GoogleVertex(270, 303),
                new GoogleVertex(270, 332),
                new GoogleVertex(182, 332)
        ));
        GoogleTextAnnotation googleTextAnnotation5 = new GoogleTextAnnotation("", "product5", new GoogleBoundingPoly(googleVertexList5));

        List<GoogleTextAnnotation> googleTextAnnotationList = new ArrayList<>(Arrays.asList(
                googleTextAnnotation1,
                googleTextAnnotation2,
                googleTextAnnotation3,
                googleTextAnnotation4,
                googleTextAnnotation5
        ));
        GoogleResponses googleResponses = new GoogleResponses(googleTextAnnotationList);
        List<GoogleResponses> googleResponsesList = new ArrayList<>();
        googleResponsesList.add(googleResponses);
        GoogleResponse googleResponse = new GoogleResponse(googleResponsesList);

        LinearFunction linearFunction1 = new LinearFunction(0, 86);
        LinearFunction linearFunction2 = new LinearFunction(20000, -6060000);
        LinearFunction linearFunction3 = new LinearFunction(0, 203);
        LinearFunction linearFunction4 = new LinearFunction(20000, -1860000);
        List<LinearFunction> linearFunctionList = new ArrayList<>(Arrays.asList(
                linearFunction1,
                linearFunction2,
                linearFunction3,
                linearFunction4
        ));
        Quadrangle quadrangle = new Quadrangle();
        quadrangle.setLinearFunctionList(linearFunctionList);

        //When
        GoogleResponses returnGoogleResponses = TextExtractor.getGoogleResponsesFromQuadrangle(quadrangle, googleResponse);

        //Then
        System.out.println(returnGoogleResponses.toString());
    }

    @Test
    public void isInQuadrangle() {
        //Given
        GoogleVertex googleVertex1 = new GoogleVertex(90, 851);
        GoogleVertex googleVertex2 = new GoogleVertex(1259, 786);
        GoogleVertex googleVertex3 = new GoogleVertex(1, 2);
        GoogleVertex googleVertex4 = new GoogleVertex(2000, 3500);
        GoogleVertex googleVertex5 = new GoogleVertex(90, 988);

        LinearFunction linearFunction1 = new LinearFunction(0.014, 717.854);
        LinearFunction linearFunction2 = new LinearFunction(138, -90143);
        LinearFunction linearFunction3 = new LinearFunction(-0.015, 2391.792);
        LinearFunction linearFunction4 = new LinearFunction(1000, 0);
        List<LinearFunction> linearFunctions = new ArrayList<>(Arrays.asList(
                linearFunction1,
                linearFunction2,
                linearFunction3,
                linearFunction4
        ));
        Quadrangle quadrangle = new Quadrangle();
        quadrangle.setLinearFunctionList(linearFunctions);

        //When
        boolean returnBoolean1 = TextExtractor.isInQuadrangle(googleVertex1, quadrangle);
        boolean returnBoolean2 = TextExtractor.isInQuadrangle(googleVertex2, quadrangle);
        boolean returnBoolean3 = TextExtractor.isInQuadrangle(googleVertex3, quadrangle);
        boolean returnBoolean4 = TextExtractor.isInQuadrangle(googleVertex4, quadrangle);
        boolean returnBoolean5 = TextExtractor.isInQuadrangle(googleVertex5, quadrangle);

        //Then
        Assert.assertEquals(true, returnBoolean1);
        Assert.assertEquals(false, returnBoolean2);
        Assert.assertEquals(false, returnBoolean3);
        Assert.assertEquals(false, returnBoolean4);
        Assert.assertEquals(true, returnBoolean5);


    }
}