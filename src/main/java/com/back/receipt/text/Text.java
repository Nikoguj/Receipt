package com.back.receipt.text;

import com.back.receipt.google.domain.GoogleResponses;
import org.springframework.stereotype.Component;

@Component
public class Text {

    public static void deleteWord(GoogleResponses googleResponses, final String word) {
        for (int i = 0; i < googleResponses.getTextAnnotations().size(); i++) {
            if(googleResponses.getTextAnnotations().get(i).getDescription().equals(word)){
                googleResponses.getTextAnnotations().remove(i);
            }
        }
    }
}