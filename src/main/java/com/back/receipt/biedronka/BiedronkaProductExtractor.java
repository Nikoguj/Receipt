package com.back.receipt.biedronka;

import com.back.receipt.domain.Product;
import com.back.receipt.google.domain.*;
import com.back.receipt.math.LinearFunction;
import com.back.receipt.math.Quadrangle;
import com.back.receipt.text.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.back.receipt.container.WordsContainer.*;

@Component
public class BiedronkaProductExtractor {

    @Autowired
    private Finder finder;

    public List<Product> extract(final GoogleResponse googleResponse) {

        GoogleResponses productGoogleResponses = extractProductName(googleResponse);

        List<GoogleTextAnnotation> googleTextAnnotationProducts = Liner.googleLinerWords(productGoogleResponses, googleResponse);

        GoogleResponses productGoogleResponses2 = extractProductPrice(googleResponse);

        List<GoogleTextAnnotation> googleTextAnnotationPrices = Liner.googleLinerWords(productGoogleResponses2, googleResponse);


        List<Product> productList = new ArrayList<>();

        int iPrices = 0;
        for (int i = 0; i < googleTextAnnotationProducts.size(); i++) {
            String productName = googleTextAnnotationProducts.get(i).getDescription();
            Product product = new Product(productName);
            String s1 = googleTextAnnotationPrices.get(iPrices).getDescription();
            if (i + 1 < googleTextAnnotationProducts.size() && googleTextAnnotationProducts.get(i + 1).getDescription().equals(rabat)) {
                String s2 = googleTextAnnotationPrices.get(iPrices + 1).getDescription();
                String s3 = googleTextAnnotationPrices.get(iPrices + 2).getDescription();

                List<Double> doubleList = TextPrice.dividerPrice(s1);
                double d1 = doubleList.get(0);
                double d2 = doubleList.get(1);
                double d3 = doubleList.get(2);

                s2 = s2.replaceAll("-", "");
                s2 = s2.replaceAll(",", ".");
                double d4 = Double.parseDouble(s2);

                s3 = s3.substring(0, s3.length() - 1);
                s3 = s3.replaceAll(",", ".");
                double d5 = Double.parseDouble(s3);

                product.setQuantity(d1);
                product.setPriceForOne(d2);
                product.setPriceWithoutDiscount(d3);
                product.setDiscount(d4);
                product.setPrice(d5);

                i++;
                iPrices = iPrices + 2;
            } else {
                List<Double> doubleList = TextPrice.dividerPrice(s1);
                double d1 = doubleList.get(0);
                double d2 = doubleList.get(1);
                double d3 = doubleList.get(2);

                product.setQuantity(d1);
                product.setPriceForOne(d2);
                product.setPrice(d3);
            }
            productList.add(product);
            iPrices++;
        }

        return productList;
    }



    private GoogleResponses extractProductPrice(final GoogleResponse googleResponse) {
        GoogleBoundingPoly receiptBoundingPoly = finder.getBoundingPolyWord(googleResponse, paragon);
        GoogleBoundingPoly soldABoundingPoly = finder.getBoundingPolyWords(googleResponse, sprzedaz_opodatkowana, 2);
        GoogleBoundingPoly fiscalBoundingPoly = finder.getBoundingPolyWord(googleResponse, fiskalny);

        LinearFunction linearFunction1 = new LinearFunction();
        linearFunction1.calculateAB(fiscalBoundingPoly.getVertices().get(2), receiptBoundingPoly.getVertices().get(3));

        LinearFunction linearFunction2 = new LinearFunction();
        linearFunction2.calculateAB(googleResponse.getGoogleResponsesList().get(0).getTextAnnotations().get(0).getBoundingPoly().getVertices().get(1),
                googleResponse.getGoogleResponsesList().get(0).getTextAnnotations().get(0).getBoundingPoly().getVertices().get(2));

        LinearFunction linearFunction3 = new LinearFunction();
        linearFunction3.calculateAB(soldABoundingPoly.getVertices().get(0), soldABoundingPoly.getVertices().get(1));

        LinearFunction linearFunction4 = new LinearFunction();
        linearFunction4.calculateAB(fiscalBoundingPoly.getVertices().get(0), fiscalBoundingPoly.getVertices().get(3));

        List<LinearFunction> linearFunctionList2 = new ArrayList<>(Arrays.asList(
                linearFunction1, linearFunction2, linearFunction3, linearFunction4
        ));

        Quadrangle quadrangle = new Quadrangle(linearFunctionList2, googleResponse);
        quadrangle.calculatePointsBoundingPoly();
        quadrangle.sortGoogleBoundingPoly();

        GoogleResponses productGoogleResponses = TextExtractor.getGoogleResponsesFromQuadrangle(quadrangle, googleResponse);

        Text.deleteWord(productGoogleResponses, fiskalny);
        return productGoogleResponses;
    }

    private GoogleResponses extractProductName(final GoogleResponse googleResponse) {
        GoogleBoundingPoly receiptBoundingPoly = finder.getBoundingPolyWord(googleResponse, paragon);
        GoogleBoundingPoly soldABoundingPoly = finder.getBoundingPolyWords(googleResponse, sprzedaz_opodatkowana, 2);

        GoogleBoundingPoly soldBBoundingPoly = receiptBoundingPoly;

        LinearFunction linearFunction1 = new LinearFunction();
        linearFunction1.calculateAB(receiptBoundingPoly.getVertices().get(2), receiptBoundingPoly.getVertices().get(3));

        LinearFunction linearFunction2 = new LinearFunction();
        linearFunction2.calculateAB(soldABoundingPoly.getVertices().get(2), soldBBoundingPoly.getVertices().get(2));

        LinearFunction linearFunction3 = new LinearFunction();
        linearFunction3.calculateAB(soldABoundingPoly.getVertices().get(0), soldABoundingPoly.getVertices().get(1));

        LinearFunction linearFunction4 = new LinearFunction(1000, 0);

        List<LinearFunction> linearFunctionList = new ArrayList<>(Arrays.asList(
                linearFunction1, linearFunction2, linearFunction3, linearFunction4
        ));

        Quadrangle quadrangle = new Quadrangle(linearFunctionList, googleResponse);
        quadrangle.calculatePointsBoundingPoly();
        quadrangle.sortGoogleBoundingPoly();

        GoogleResponses productGoogleResponses = TextExtractor.getGoogleResponsesFromQuadrangle(quadrangle, googleResponse);

        Text.deleteWord(productGoogleResponses, a);
        Text.deleteWord(productGoogleResponses, opodatkowana);
        Text.deleteWord(productGoogleResponses, sprzedaz);
        Text.deleteWord(productGoogleResponses, paragon);
        return productGoogleResponses;
    }


}