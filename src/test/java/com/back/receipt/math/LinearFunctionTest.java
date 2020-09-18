package com.back.receipt.math;


import com.back.receipt.google.domain.GoogleVertex;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class LinearFunctionTest {

    @Test
    public void calculateAB() {
        //Given
        GoogleVertex googleVertex1 = new GoogleVertex(0, -10);
        GoogleVertex googleVertex2 = new GoogleVertex(2, 0);

        //When
        LinearFunction function = new LinearFunction();
        function.calculateAB(googleVertex1, googleVertex2);

        //Then
        Assert.assertEquals(5, function.getA(), 0.005);
        Assert.assertEquals(-10, function.getB(), 0.005);
    }
}