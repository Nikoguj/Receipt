package com.back.receipt.biedronka;

import com.back.receipt.google.domain.GoogleBoundingPoly;
import com.back.receipt.google.domain.GoogleResponse;
import com.back.receipt.google.domain.GoogleVertex;
import com.back.receipt.math.LinearFunction;
import com.back.receipt.math.Quadrangle;
import com.back.receipt.text.Finder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductExtractor {

    @Autowired
    private Finder finder;

    public void extract(GoogleResponse googleResponse) {

        GoogleBoundingPoly receiptBoundingPoly = finder.getBoundingPolyWord(googleResponse, "PARAGON");
        GoogleBoundingPoly soldA = finder.getThirdBoundingPolyAfterWord(googleResponse, "SPRZEDAZ OPODATKOWANA A");
        GoogleBoundingPoly soldB = finder.getThirdBoundingPolyAfterWord(googleResponse, "SPRZEDAZ OPODATKOWANA B");

        LinearFunction linearFunction1 = new LinearFunction();
        linearFunction1.calculateAB(receiptBoundingPoly.getVertices().get(2), receiptBoundingPoly.getVertices().get(3));

        LinearFunction linearFunction2 = new LinearFunction();
        linearFunction2.calculateAB(soldA.getVertices().get(2), soldB.getVertices().get(2));

        LinearFunction linearFunction3 = new LinearFunction();
        linearFunction3.calculateAB(soldA.getVertices().get(0), soldA.getVertices().get(1));

        LinearFunction linearFunction4 = new LinearFunction(1000, 0);

        List<LinearFunction> linearFunctionList = new ArrayList<>(Arrays.asList(
                linearFunction1, linearFunction2, linearFunction3, linearFunction4
        ));

        Quadrangle quadrangle = new Quadrangle(linearFunctionList, 1617, 3253);
        quadrangle.calculatePointsBoundingPoly();
        quadrangle.sortGoogleBoundingPoly();



    }
}