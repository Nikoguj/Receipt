package com.back.receipt.google.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
public class GoogleTextAnnotationDto {

    @JsonProperty("locale")
    private String locale;
    @JsonProperty("description")
    private String description;
    @JsonProperty("boundingPoly")
    private GoogleBoundingPolyDto boundingPoly;

    @Override
    public String toString() {
        return "GoogleTextAnnotationDto{ locale:" + locale + "\n description  {" + description + "}, boundingPoly:" + boundingPoly + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleTextAnnotationDto that = (GoogleTextAnnotationDto) o;

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
