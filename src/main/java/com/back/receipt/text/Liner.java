package com.back.receipt.text;

import com.back.receipt.google.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class Liner {

    public static List<GoogleTextAnnotation> googleLinerWords(final GoogleResponses myGoogleResponses, final GoogleResponse googleResponse) {

        String fullText = googleResponse.getGoogleResponsesList().get(0).getTextAnnotations().get(0).getDescription();
        fullText = clearFullText(fullText);
        clearGoogleResponse(myGoogleResponses);

        List<GoogleTextAnnotation> googleTextAnnotationList = new ArrayList<>();

        for (int i = 0; i < myGoogleResponses.getTextAnnotations().size(); i++) {
            String currentDescription = myGoogleResponses.getTextAnnotations().get(i).getDescription();
            if(currentDescription.equals("Rabat")) {
                GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation();
                googleTextAnnotation.setDescription("Rabat");
                googleTextAnnotationList.add(googleTextAnnotation);
            } else if (currentDescription.length() > 3 && StringUtils.countOccurrencesOf(fullText, currentDescription) == 1) {
                int indexS = fullText.indexOf(currentDescription);
                addLine(fullText, googleTextAnnotationList, indexS);
            } else {
                try {
                    currentDescription = myGoogleResponses.getTextAnnotations().get(i).getDescription() + " " + myGoogleResponses.getTextAnnotations().get(i + 1).getDescription();
                    int indexS = fullText.indexOf(currentDescription);
                    if (indexS != -1) {
                        addLine(fullText, googleTextAnnotationList, indexS);
                    }
                } catch (Exception e) {
                    currentDescription = myGoogleResponses.getTextAnnotations().get(i).getDescription();
                    int indexS = fullText.indexOf(currentDescription);
                    if (indexS != -1) {
                        addLine(fullText, googleTextAnnotationList, indexS);
                    }
                }
            }

        }

        return googleTextAnnotationList;
    }

    private static void addLine(String fullText, List<GoogleTextAnnotation> googleTextAnnotationList, int indexS) {
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