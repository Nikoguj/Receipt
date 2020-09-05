package com.back.receipt.google.domain;

import com.back.receipt.google.domain.dto.GoogleBoundingPolyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleTextAnnotation {

    private String locale;
    private String description;
    private GoogleBoundingPoly boundingPoly;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleTextAnnotation that = (GoogleTextAnnotation) o;

        if (locale != null ? !locale.equals(that.locale) : that.locale != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return boundingPoly != null ? boundingPoly.equals(that.boundingPoly) : that.boundingPoly == null;
    }

    @Override
    public int hashCode() {
        int result = locale != null ? locale.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (boundingPoly != null ? boundingPoly.hashCode() : 0);
        return result;
    }
}
