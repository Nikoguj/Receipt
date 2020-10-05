package com.back.receipt.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextPrice {

    public static List<Double> dividerPrice(final String text) {
        int space1 = text.indexOf(" ");
        String s1 = text.substring(0, space1);
        String s2 = text.substring(space1 + 1);
        int space2 = s2.indexOf(" ");
        s2 = s2.substring(0, space2);
        String s3 = text.substring(space1 + space2 + 2);
        s2 = s2.substring(1);
        s3 = s3.substring(0, s3.length() - 1);

        s1 = s1.replaceAll(",", ".");
        s2 = s2.replaceAll(",", ".");
        s3 = s3.replaceAll(",", ".");

        Double d1 = Double.parseDouble(s1);
        Double d2 = Double.parseDouble(s2);
        Double d3 = Double.parseDouble(s3);

        return new ArrayList<>(Arrays.asList(d1, d2, d3));

    }
}
