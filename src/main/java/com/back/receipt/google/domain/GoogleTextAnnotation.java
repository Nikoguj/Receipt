package com.back.receipt.google.domain;

import com.back.receipt.google.domain.dto.GoogleBoundingPolyDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GoogleTextAnnotation {

    private String locale;
    private String description;
    private GoogleBoundingPoly boundingPoly;

}
