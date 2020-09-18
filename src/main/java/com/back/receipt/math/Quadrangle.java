package com.back.receipt.math;

import com.back.receipt.google.domain.GoogleBoundingPoly;
import com.back.receipt.google.domain.GoogleVertex;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
public class Quadrangle {

    private List<LinearFunction> linearFunctionList = new ArrayList<>();
    private int maxX;
    private int maxY;
    private GoogleBoundingPoly googleBoundingPoly;

    public Quadrangle(List<LinearFunction> linearFunctionList, int maxX, int maxY) {
        this.linearFunctionList = linearFunctionList;
        this.maxX = maxX;
        this.maxY = maxY;
        googleBoundingPoly = new GoogleBoundingPoly();

    }

    public void calculatePointsBoundingPoly() {
        for (LinearFunction linearFunction1 : linearFunctionList) {
            for (LinearFunction linearFunction2 : linearFunctionList) {
                Optional<GoogleVertex> googleVertex = calculateIntersectionPoint(linearFunction1, linearFunction2);
                if (googleVertex.isPresent()) {
                    if (!isInsideList(googleVertex.get())) {
                        if (googleVertex.get().getX() <= maxX && googleVertex.get().getX() >= 0 && googleVertex.get().getY() <= maxY) {
                            googleBoundingPoly.getVertices().add(googleVertex.get());
                        }
                    }
                }
            }
        }
    }

    private boolean isInsideList(GoogleVertex googleVertex){
        for (int i = 0; i < googleBoundingPoly.getVertices().size(); i++) {
            if(googleVertex.equals(googleBoundingPoly.getVertices().get(i))) {
                return true;
            }
        }
        return false;
    }

    private Optional<GoogleVertex> calculateIntersectionPoint(LinearFunction linearFunction1, LinearFunction linearFunction2) {

        if (linearFunction1.getA() == linearFunction2.getA()) {
            return Optional.empty();
        }

        double x = (linearFunction2.getB() - linearFunction1.getB()) / (linearFunction1.getA() - linearFunction2.getA());
        double y = linearFunction1.getA() * x + linearFunction1.getB();

        GoogleVertex point = new GoogleVertex((int) Math.round(x), (int) Math.round(y));
        return Optional.of(point);
    }

    public void sortGoogleBoundingPoly() {
        List<GoogleVertex> googleVertexList = new ArrayList<>();
        GoogleVertex first = findFirst();
        googleVertexList.add(first);
        googleBoundingPoly.getVertices().remove(first);

        GoogleVertex second = findSecond(first);
        googleVertexList.add(second);
        googleBoundingPoly.getVertices().remove(second);

        GoogleVertex fourth = findFourth(first);
        googleBoundingPoly.getVertices().remove(fourth);

        googleVertexList.add(googleBoundingPoly.getVertices().get(0));
        googleVertexList.add(fourth);

        googleBoundingPoly.getVertices().clear();
        googleBoundingPoly.getVertices().addAll(googleVertexList);

    }

    private GoogleVertex findFirst() {
        GoogleVertex returnGoogleVertex = new GoogleVertex();
        double minD = 0;
        for(GoogleVertex googleVertex: googleBoundingPoly.getVertices()) {
            double d = getD(googleVertex);
            if(d < minD || minD == 0) {
                returnGoogleVertex = googleVertex;
                minD = d;
            }
        }
        return returnGoogleVertex;
    }

    private GoogleVertex findFourth(GoogleVertex firstGoogleVertex) {
        GoogleVertex closerGoogleVertex = new GoogleVertex();
        int x = 0;
        for(GoogleVertex googleVertex: googleBoundingPoly.getVertices()) {
            int distance =  Math.abs(firstGoogleVertex.getX() - googleVertex.getX());
            if(distance <= x || x == 0) {
                closerGoogleVertex = googleVertex;
                x = distance;
            }
        }
        return closerGoogleVertex;
    }

    private GoogleVertex findSecond(GoogleVertex firstGoogleVertex) {
        GoogleVertex closerGoogleVertex = new GoogleVertex();
        int y = 0;
        for(GoogleVertex googleVertex: googleBoundingPoly.getVertices()) {
            int distance =  Math.abs(firstGoogleVertex.getY() - googleVertex.getY());
            if(distance <= y || y == 0) {
                closerGoogleVertex = googleVertex;
                y = distance;
            }
        }
        return closerGoogleVertex;
    }

    private double getD(GoogleVertex googleVertex) {
        return Math.sqrt(googleVertex.getX()*googleVertex.getX() + googleVertex.getY()*googleVertex.getY());
    }

}