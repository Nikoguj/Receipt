package com.back.receipt.text;

import com.back.receipt.google.domain.*;
import com.back.receipt.math.Quadrangle;
import org.springframework.stereotype.Component;

@Component
public class TextExtractor {

    public static GoogleResponses getGoogleResponsesFromQuadrangle(final Quadrangle quadrangle, final GoogleResponse googleResponse) {
        GoogleResponses googleResponses = new GoogleResponses();
        for (GoogleTextAnnotation googleTextAnnotation : googleResponse.getGoogleResponsesList().get(0).getTextAnnotations()) {
            for(GoogleVertex googleVertex: googleTextAnnotation.getBoundingPoly().getVertices()) {
                if(isInQuadrangle(googleVertex, quadrangle)) {
                    googleResponses.getTextAnnotations().add(googleTextAnnotation);
                    break;
                }
            }
        }
        return googleResponses;
    }

    public static boolean isInQuadrangle(final GoogleVertex googleVertex, final Quadrangle quadrangle) {
        double yBigger = quadrangle.getLinearFunctionList().get(0).getA() * googleVertex.getX() + quadrangle.getLinearFunctionList().get(0).getB();

        if (yBigger > googleVertex.getY()) {
            return false;
        }

        double ySmaller = quadrangle.getLinearFunctionList().get(2).getA() * googleVertex.getX() + quadrangle.getLinearFunctionList().get(2).getB();

        if (ySmaller < googleVertex.getY()) {
            return false;
        }

        double xSmaller = quadrangle.getLinearFunctionList().get(1).getA() * googleVertex.getX() + quadrangle.getLinearFunctionList().get(1).getB();

        if (quadrangle.getLinearFunctionList().get(1).getA() > 0) {
            if (xSmaller < googleVertex.getY()) {
            } else {
                return false;
            }
        } else if (quadrangle.getLinearFunctionList().get(1).getA() < 0) {
            if (xSmaller > googleVertex.getY()) {
            } else {
                return false;
            }
        }

        double xBigger = quadrangle.getLinearFunctionList().get(3).getA() * googleVertex.getX() + quadrangle.getLinearFunctionList().get(3).getB();

        if (quadrangle.getLinearFunctionList().get(3).getA() > 0) {
            if (xBigger > googleVertex.getY()) {
            } else {
                return false;
            }
        } else if (quadrangle.getLinearFunctionList().get(3).getA() < 0) {
            if (xBigger < googleVertex.getY()) {
            } else {
                return false;
            }
        }

        return true;

    }

}
