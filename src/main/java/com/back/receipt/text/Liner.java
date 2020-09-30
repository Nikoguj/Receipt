package com.back.receipt.text;

import com.back.receipt.google.domain.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Liner {

    public static List<GoogleTextAnnotation> googleLinerWords(GoogleResponses myGoogleResponses, GoogleResponse googleResponse) {

        String fullText = googleResponse.getGoogleResponsesList().get(0).getTextAnnotations().get(0).getDescription();
        fullText = clearFullText(fullText);
        clearGoogleResponse(myGoogleResponses);

        List<GoogleTextAnnotation> googleTextAnnotationList = new ArrayList<>();

        for (int i = 0; i < myGoogleResponses.getTextAnnotations().size(); i++) {
            String currentDescription = myGoogleResponses.getTextAnnotations().get(i).getDescription();
            if (currentDescription.length() > 3) {
                int indexS = fullText.indexOf(currentDescription);
                addName(fullText, googleTextAnnotationList, indexS);
            } else {
                try {
                    currentDescription = myGoogleResponses.getTextAnnotations().get(i).getDescription() + " " + myGoogleResponses.getTextAnnotations().get(i + 1).getDescription();
                    int indexS = fullText.indexOf(currentDescription);
                    if(indexS != -1) {
                        addName(fullText, googleTextAnnotationList, indexS);
                    }
                } catch (Exception e) {
                    currentDescription = myGoogleResponses.getTextAnnotations().get(i).getDescription();
                    int indexS = fullText.indexOf(currentDescription);
                    if(indexS != -1) {
                        addName(fullText, googleTextAnnotationList, indexS);
                    }
                }
            }

        }

        return googleTextAnnotationList;
    }

    private static void addName(String fullText, List<GoogleTextAnnotation> googleTextAnnotationList, int indexS) {
        StringBuilder currentS = new StringBuilder();
        if (fullText.charAt(indexS - 1) == '\n') {

            while (fullText.charAt(indexS) != '\n') {
                currentS.append(fullText.charAt(indexS));
                indexS++;
            }
            googleTextAnnotationList.add(new GoogleTextAnnotation("", currentS.toString(), new GoogleBoundingPoly()));
        }
    }

    private static String clearFullText(String fullText) {
        StringBuilder builder = new StringBuilder(fullText);
        for (int i = 0; i < fullText.length(); i++) {
            if (builder.charAt(i) == '/') {
                builder.setCharAt(i, ' ');
            }
        }
        return builder.toString();
    }

    private static void clearGoogleResponse(GoogleResponses myGoogleResponse) {
        myGoogleResponse.getTextAnnotations().removeIf(googleTextAnnotation -> googleTextAnnotation.getDescription().equals("/"));
    }
}