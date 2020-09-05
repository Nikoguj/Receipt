package com.back.receipt.google.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleBoundingPoly {

    private List<GoogleVertex> vertices = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleBoundingPoly that = (GoogleBoundingPoly) o;

        return vertices != null ? vertices.equals(that.vertices) : that.vertices == null;
    }

    @Override
    public int hashCode() {
        return vertices != null ? vertices.hashCode() : 0;
    }
}
