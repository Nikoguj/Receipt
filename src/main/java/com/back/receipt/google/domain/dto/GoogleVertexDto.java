package com.back.receipt.google.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
public class GoogleVertexDto {

    @JsonProperty("x")
    private Integer x;
    @JsonProperty("y")
    private Integer y;

    @Override
    public String toString() {
        return "GoogleVertexDto{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleVertexDto that = (GoogleVertexDto) o;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        return y != null ? y.equals(that.y) : that.y == null;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
