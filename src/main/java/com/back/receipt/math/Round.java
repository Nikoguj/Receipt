package com.back.receipt.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Round {

    public static double roundDouble(double d, int places) {

        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
