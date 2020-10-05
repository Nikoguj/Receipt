package com.back.receipt.google.mapper;

import com.back.receipt.google.domain.*;
import com.back.receipt.google.domain.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleMapperTest {

    @Autowired
    private GoogleMapper googleMapper;

    private static GoogleVertexDto googleVertexDto;
    private static GoogleVertex googleVertex;

    @BeforeEach
    public void initialize() {
        googleVertexDto  = new GoogleVertexDto(50, 100);
        googleVertex = new GoogleVertex(50, 100);
    }

    @Test
    public void mapToGoogleVertex() {
        //When
        GoogleVertex returnVertex = googleMapper.mapToGoogleVertex(googleVertexDto);

        //Then
        assertEquals(returnVertex, googleVertex);
    }

    @Test
    public void mapToGoogleBoundingPoly() {
        //Given
        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Collections.singletonList(googleVertexDto)));

        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(new ArrayList<>(Collections.singletonList(googleVertex)));

        //When
        GoogleBoundingPoly returnBoundingPoly = googleMapper.mapToGoogleBoundingPoly(googleBoundingPolyDto);

        //Then
        assertEquals(googleBoundingPoly, returnBoundingPoly);
    }

    @Test
    public void mapToGoogleTextAnnotation() {
        //Given
        //Given
        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Collections.singletonList(googleVertexDto)));
        GoogleTextAnnotationDto googleTextAnnotationDto = new GoogleTextAnnotationDto("id", "hi", googleBoundingPolyDto);

        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(new ArrayList<>(Collections.singletonList(googleVertex)));
        GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation("id", "hi", googleBoundingPoly);

        //When
        GoogleTextAnnotation returnTextAnnotation = googleMapper.mapToGoogleTextAnnotation(googleTextAnnotationDto);

        //Then
        assertEquals(googleTextAnnotation, returnTextAnnotation);
    }

    @Test
    public void mapToGoogleResponses() {
        //Given
        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Collections.singletonList(googleVertexDto)));
        GoogleTextAnnotationDto googleTextAnnotationDto = new GoogleTextAnnotationDto("id", "hi", googleBoundingPolyDto);
        GoogleResponsesDto googleResponsesDto = new GoogleResponsesDto(new ArrayList<>(Collections.singletonList(googleTextAnnotationDto)));

        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(new ArrayList<>(Collections.singletonList(googleVertex)));
        GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation("id", "hi", googleBoundingPoly);
        GoogleResponses googleResponses = new GoogleResponses(new ArrayList<>(Collections.singletonList(googleTextAnnotation)));

        //When
        GoogleResponses returnResponses = googleMapper.mapToGoogleResponses(googleResponsesDto);

        //Then
        assertEquals(googleResponses, returnResponses);
    }

    @Test
    public void mapToGoogleResponse() {
        //Given
        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Collections.singletonList(googleVertexDto)));
        GoogleTextAnnotationDto googleTextAnnotationDto = new GoogleTextAnnotationDto("id", "hi", googleBoundingPolyDto);
        GoogleResponsesDto googleResponsesDto = new GoogleResponsesDto(new ArrayList<>(Collections.singletonList(googleTextAnnotationDto)));
        GoogleResponseDto googleResponseDto = new GoogleResponseDto(new ArrayList<>(Collections.singletonList(googleResponsesDto)));

        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(new ArrayList<>(Collections.singletonList(googleVertex)));
        GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation("id", "hi", googleBoundingPoly);
        GoogleResponses googleResponses = new GoogleResponses(new ArrayList<>(Collections.singletonList(googleTextAnnotation)));
        GoogleResponse googleResponse = new GoogleResponse(new ArrayList<>(Collections.singletonList(googleResponses)));

        //When
        GoogleResponse returnResponse = googleMapper.mapToGoogleResponse(googleResponseDto);

        //Then
        assertEquals(googleResponse, returnResponse);
    }
}