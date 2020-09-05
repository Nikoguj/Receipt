package com.back.receipt.google.mapper;

import com.back.receipt.google.domain.*;
import com.back.receipt.google.domain.dto.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GoogleMapper {

    public GoogleVertex mapToGoogleVertex(GoogleVertexDto googleVertexDto) {
        return new GoogleVertex(googleVertexDto.getX(), googleVertexDto.getY());
    }

    public GoogleBoundingPoly mapToGoogleBoundingPoly(GoogleBoundingPolyDto googleBoundingPolyDto) {
        return new GoogleBoundingPoly(
                googleBoundingPolyDto.getVertices().stream()
                        .map(this::mapToGoogleVertex)
                        .collect(Collectors.toList())
        );
    }

    public GoogleTextAnnotation mapToGoogleTextAnnotation(GoogleTextAnnotationDto googleTextAnnotationDto) {
        return new GoogleTextAnnotation(
                googleTextAnnotationDto.getLocale(),
                googleTextAnnotationDto.getDescription(),
                mapToGoogleBoundingPoly(googleTextAnnotationDto.getBoundingPoly()));
    }

    public GoogleResponses mapToGoogleResponses(GoogleResponsesDto googleResponsesDto) {
        return new GoogleResponses(
                googleResponsesDto.getTextAnnotations().stream()
                        .map(this::mapToGoogleTextAnnotation)
                        .collect(Collectors.toList())
        );
    }

    public GoogleResponse mapToGoogleResponse(GoogleResponseDto googleResponseDto) {
        return new GoogleResponse(
                googleResponseDto.getGoogleResponsesDtoList().stream()
                        .map(this::mapToGoogleResponses)
                        .collect(Collectors.toList())
        );
    }
}