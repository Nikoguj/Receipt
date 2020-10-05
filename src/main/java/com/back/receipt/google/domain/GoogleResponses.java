package com.back.receipt.google.domain;

import com.back.receipt.google.domain.dto.GoogleTextAnnotationDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GoogleResponses {

    private List<GoogleTextAnnotation> textAnnotations = new ArrayList<>();
}
