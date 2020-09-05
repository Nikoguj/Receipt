package com.back.receipt.google.domain;

import com.back.receipt.google.domain.dto.GoogleTextAnnotationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleResponses {

    private List<GoogleTextAnnotation> textAnnotations = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleResponses that = (GoogleResponses) o;

        return textAnnotations != null ? textAnnotations.equals(that.textAnnotations) : that.textAnnotations == null;
    }

    @Override
    public int hashCode() {
        return textAnnotations != null ? textAnnotations.hashCode() : 0;
    }
}
