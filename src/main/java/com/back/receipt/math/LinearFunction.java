package com.back.receipt.math;

import com.back.receipt.google.domain.GoogleVertex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class LinearFunction {
    private double a;
    private double b;

    public void calculateAB(GoogleVertex googleVertex1, GoogleVertex googleVertex2) {
        if((googleVertex2.getX()-googleVertex1.getX()) != 0) {
            a = ((double)googleVertex2.getY()-(double)googleVertex1.getY())/((double)googleVertex2.getX()-(double)googleVertex1.getX());
        } else {
            a = 10000;
        }
        b = googleVertex1.getY() - (a * googleVertex1.getX());
    }
}
