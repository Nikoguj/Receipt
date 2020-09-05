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
public class GoogleBoundingPolyDto {

    @JsonProperty("vertices")
    private List<GoogleVertexDto> vertices = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleBoundingPolyDto that = (GoogleBoundingPolyDto) o;

        return vertices != null ? vertices.equals(that.vertices) : that.vertices == null;
    }

    @Override
    public int hashCode() {
        return vertices != null ? vertices.hashCode() : 0;
    }
}
