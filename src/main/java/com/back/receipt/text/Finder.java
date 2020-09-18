package com.back.receipt.text;

import com.back.receipt.google.domain.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@NoArgsConstructor
@Component
public class Finder extends Exception{

    public GoogleBoundingPoly getBoundingPolyWord(final GoogleResponse googleResponse, final String word) {
        Optional<GoogleTextAnnotation> googleTextAnnotation = googleResponse.getGoogleResponsesList().get(0).getTextAnnotations().stream()
                .filter(googleTextAnnotation1 -> googleTextAnnotation1.getDescription().equals(word))
                .findFirst();

        return googleTextAnnotation.get().getBoundingPoly();
    }

    public GoogleBoundingPoly getThirdBoundingPolyAfterWord (final GoogleResponse googleResponse, final String words) {
        GoogleResponses googleResponses = googleResponse.getGoogleResponsesList().get(0);
        for (int i = 0; i < googleResponses.getTextAnnotations().size(); i++) {
            if(googleResponses.getTextAnnotations().size() > i-2) {
                String threeWords = googleResponses.getTextAnnotations().get(i).getDescription() + " " +
                        googleResponses.getTextAnnotations().get(i+1).getDescription() + " " +
                        googleResponses.getTextAnnotations().get(i+2).getDescription();

                if(threeWords.equals(words)) {
                    GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly();

                    GoogleVertex googleVertex1 = new GoogleVertex(googleResponses.getTextAnnotations().get(i).getBoundingPoly().getVertices().get(0).getX(),
                            googleResponses.getTextAnnotations().get(i).getBoundingPoly().getVertices().get(0).getY());

                    GoogleVertex googleVertex2 = new GoogleVertex(googleResponses.getTextAnnotations().get(i).getBoundingPoly().getVertices().get(3).getX(),
                            googleResponses.getTextAnnotations().get(i).getBoundingPoly().getVertices().get(3).getY());

                    GoogleVertex googleVertex3 = new GoogleVertex(googleResponses.getTextAnnotations().get(i+2).getBoundingPoly().getVertices().get(1).getX(),
                            googleResponses.getTextAnnotations().get(i).getBoundingPoly().getVertices().get(1).getY());

                    GoogleVertex googleVertex4 = new GoogleVertex(googleResponses.getTextAnnotations().get(i+2).getBoundingPoly().getVertices().get(2).getX(),
                            googleResponses.getTextAnnotations().get(i).getBoundingPoly().getVertices().get(2).getY());

                    googleBoundingPoly.getVertices().add(googleVertex1);
                    googleBoundingPoly.getVertices().add(googleVertex3);
                    googleBoundingPoly.getVertices().add(googleVertex4);
                    googleBoundingPoly.getVertices().add(googleVertex2);

                    return googleBoundingPoly;
                }
            }
        }
        return null;
    }

}