package com.back.receipt.math;


import com.back.receipt.google.domain.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuadrangleTest {

    @Test
    public void calculatePointsBoundingPoly() {
        //Given
        List<LinearFunction> linearFunctionList = new ArrayList<LinearFunction>(Arrays.asList(
                new LinearFunction(0.05, -3),
                new LinearFunction(-0.05, -12),
                new LinearFunction(40, -40),
                new LinearFunction(90, -350)
        ));

        GoogleResponse googleResponse = new GoogleResponse();
        GoogleResponses googleResponses = new GoogleResponses();

        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly();
        GoogleVertex googleVertex1 = new GoogleVertex(0 , 1);
        GoogleVertex googleVertex2 = new GoogleVertex(3 , 20);
        GoogleVertex googleVertex3 = new GoogleVertex(1 , 19);
        GoogleVertex googleVertex4 = new GoogleVertex(20 , 20);
        googleBoundingPoly.getVertices().add(googleVertex1);
        googleBoundingPoly.getVertices().add(googleVertex2);
        googleBoundingPoly.getVertices().add(googleVertex3);
        googleBoundingPoly.getVertices().add(googleVertex4);

        GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation("pl", "dis", googleBoundingPoly);
        googleResponses.getTextAnnotations().add(googleTextAnnotation);
        googleResponse.getGoogleResponsesList().add(googleResponses);

        Quadrangle quadrangle = new Quadrangle(linearFunctionList, googleResponse);

        //When
        quadrangle.calculatePointsBoundingPoly();

        //Then
        Assert.assertEquals(4, quadrangle.getGoogleBoundingPoly().getVertices().size());
        Assert.assertEquals(java.util.Optional.of(1).get(), quadrangle.getGoogleBoundingPoly().getVertices().get(0).getX());
        Assert.assertEquals(java.util.Optional.of(-3).get(), quadrangle.getGoogleBoundingPoly().getVertices().get(0).getY());
        Assert.assertEquals(java.util.Optional.of(4).get(), quadrangle.getGoogleBoundingPoly().getVertices().get(1).getX());
        Assert.assertEquals(java.util.Optional.of(-3).get(), quadrangle.getGoogleBoundingPoly().getVertices().get(1).getY());
        Assert.assertEquals(java.util.Optional.of(1).get(), quadrangle.getGoogleBoundingPoly().getVertices().get(2).getX());
        Assert.assertEquals(java.util.Optional.of(-12).get(), quadrangle.getGoogleBoundingPoly().getVertices().get(2).getY());
        Assert.assertEquals(java.util.Optional.of(4).get(), quadrangle.getGoogleBoundingPoly().getVertices().get(3).getX());
        Assert.assertEquals(java.util.Optional.of(-12).get(), quadrangle.getGoogleBoundingPoly().getVertices().get(3).getY());
    }

    @Test
    public void sortTest() {
        //Given
        Quadrangle quadrangle = new Quadrangle();

        List<GoogleVertex> googleVertexList = new ArrayList<>(Arrays.asList(
                new GoogleVertex(1, 1),
                new GoogleVertex(4, 2),
                new GoogleVertex(2, 4),
                new GoogleVertex(3, 5)
        ));

        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(googleVertexList);

        quadrangle.setGoogleBoundingPoly(googleBoundingPoly);

        //When
        quadrangle.sortGoogleBoundingPoly();

        //Then
        for (GoogleVertex googleVertex: quadrangle.getGoogleBoundingPoly().getVertices()) {
            System.out.println("x: " + googleVertex.getX() + " y: " + googleVertex.getY());
        }
    }

}