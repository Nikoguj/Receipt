package com.back.receipt.google.domain;

import com.back.receipt.google.domain.dto.GoogleBoundingPolyDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GoogleTextAnnotation {

    private String locale;
    private String description;
    private GoogleBoundingPoly boundingPoly;

}
