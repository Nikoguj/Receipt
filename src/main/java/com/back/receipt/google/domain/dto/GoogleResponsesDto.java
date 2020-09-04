package com.back.receipt.google.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Setter
public class GoogleResponsesDto {

    @JsonProperty("textAnnotations")
    private List<GoogleTextAnnotationDto> textAnnotations = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleResponsesDto that = (GoogleResponsesDto) o;

        return textAnnotations != null ? textAnnotations.equals(that.textAnnotations) : that.textAnnotations == null;
    }

    @Override
    public int hashCode() {
        return textAnnotations != null ? textAnnotations.hashCode() : 0;
    }
}
