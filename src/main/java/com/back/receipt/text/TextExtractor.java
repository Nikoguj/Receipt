package com.back.receipt.text;

import com.back.receipt.google.domain.*;
import com.back.receipt.math.Quadrangle;
import org.springframework.stereotype.Component;

@Component
public class TextExtractor {

    public static GoogleResponses getGoogleResponsesFromQuadrangle(Quadrangle quadrangle, GoogleResponse googleResponse) {
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

    public static boolean isInQuadrangle(GoogleVertex googleVertex, Quadrangle quadrangle) {
        double yBigger = quadrangle.getLinearFunctionList().get(0).getA() * googleVertex.getX() + quadrangle.getLinearFunctionList().get(0).getB();
        boolean b1 = false;

        if (yBigger < googleVertex.getY()) {
            b1 = true;
        } else {
            return false;
        }

        double ySmaller = quadrangle.getLinearFunctionList().get(2).getA() * googleVertex.getX() + quadrangle.getLinearFunctionList().get(2).getB();
        boolean b2 = false;

        if (ySmaller > googleVertex.getY()) {
            b2 = true;
        } else {
            return false;
        }

        double xSmaller = quadrangle.getLinearFunctionList().get(1).getA() * googleVertex.getX() + quadrangle.getLinearFunctionList().get(1).getB();
        boolean b3 = false;

        if (quadrangle.getLinearFunctionList().get(1).getA() > 0) {
            if (xSmaller < googleVertex.getY()) {
                b3 = true;
            } else {
                return false;
            }
        } else if (quadrangle.getLinearFunctionList().get(1).getA() < 0) {
            if (xSmaller > googleVertex.getY()) {
                b3 = true;
            } else {
                return false;
            }
        }

        double xBigger = quadrangle.getLinearFunctionList().get(3).getA() * googleVertex.getX() + quadrangle.getLinearFunctionList().get(3).getB();
        boolean b4 = false;

        if (quadrangle.getLinearFunctionList().get(3).getA() > 0) {
            if (xBigger > googleVertex.getY()) {
                b3 = true;
            } else {
                return false;
            }
        } else if (quadrangle.getLinearFunctionList().get(3).getA() < 0) {
            if (xBigger < googleVertex.getY()) {
                b3 = true;
            } else {
                return false;
            }
        }

        return true;

    }

}
