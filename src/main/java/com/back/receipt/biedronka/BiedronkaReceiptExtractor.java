package com.back.receipt.biedronka;

import com.back.receipt.container.WordsContainer;
import com.back.receipt.domain.Product;
import com.back.receipt.domain.Receipt;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.google.domain.GoogleResponse;
import com.back.receipt.google.domain.GoogleTextAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;

@Component
public class BiedronkaReceiptExtractor {

    @Autowired
    private BiedronkaProductExtractor biedronkaProductExtractor;

    public Receipt extract(final GoogleResponse googleResponse) throws MyResourceNotFoundException {

        Receipt receipt = new Receipt();
        receipt.setAddress(getAddress(googleResponse));
        receipt.setDate(getDate(googleResponse));
        receipt.setProductList(biedronkaProductExtractor.extract(googleResponse));
        receipt.setFullPrice(getPrice(receipt));

        return receipt;
    }

    private String getAddress(final GoogleResponse googleResponse) {
        StringBuilder address = new StringBuilder();
        String fullText = googleResponse.getGoogleResponsesList().get(0).getTextAnnotations().get(0).getDescription();
        int startIndex = fullText.indexOf(WordsContainer.sklep);
        if (startIndex != -1) {
            fullText = fullText.substring(startIndex);
            int indexS = 0;
            while (fullText.charAt(indexS) != '\n') {
                address.append(fullText.charAt(indexS));
                indexS++;
            }
            return address.toString();
        } else {
            return "Can't find address";
        }
    }

    private String getDate(final GoogleResponse googleResponse) {
        StringBuilder date = new StringBuilder();
        for (int i = 0; i < googleResponse.getGoogleResponsesList().get(0).getTextAnnotations().size(); i++) {
            List<GoogleTextAnnotation> textAnnotations = googleResponse.getGoogleResponsesList().get(0).getTextAnnotations();
            if(textAnnotations.get(i).getDescription().length() == 10) {
                if(textAnnotations.get(i+1).getDescription().length() == 5) {
                    date.append(textAnnotations.get(i).getDescription() + " " + textAnnotations.get(i+1).getDescription());
                    break;
                }
            }
        }
        if (date.length() != 0) {
            return date.toString();
        } else {
            return "Can't find date";
        }
    }

    private double getPrice(Receipt receipt) {
        double price = 0;
        for(Product product: receipt.getProductList()) {
            price += product.getPrice();
        }
        if (price != 0) {
            return Math.round(price * 100.0) / 100.0;
        } else {
            return 0;
        }
    }
}