package com.back.receipt.google.mapper;

import com.back.receipt.google.domain.*;
import com.back.receipt.google.domain.dto.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleMapperTest {

    @Autowired
    private GoogleMapper googleMapper;

    @Test
    public void mapToGoogleVertex() {
        //Given
        GoogleVertexDto googleVertexDto = new GoogleVertexDto(50, 100);
        GoogleVertex googleVertex = new GoogleVertex(50, 100);

        //When
        GoogleVertex returnVertex = googleMapper.mapToGoogleVertex(googleVertexDto);

        //Then
        Assert.assertEquals(returnVertex, googleVertex);
    }

    @Test
    public void mapToGoogleBoundingPoly() {
        //Given
        GoogleVertexDto googleVertexDto = new GoogleVertexDto(50, 100);
        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Collections.singletonList(googleVertexDto)));


        GoogleVertex googleVertex = new GoogleVertex(50, 100);
        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(new ArrayList<>(Collections.singletonList(googleVertex)));

        //When
        GoogleBoundingPoly returnBoundingPoly = googleMapper.mapToGoogleBoundingPoly(googleBoundingPolyDto);

        //Then
        Assert.assertEquals(googleBoundingPoly, returnBoundingPoly);
    }

    @Test
    public void mapToGoogleTextAnnotation() {
        //Given
        //Given
        GoogleVertexDto googleVertexDto = new GoogleVertexDto(50, 100);
        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Collections.singletonList(googleVertexDto)));
        GoogleTextAnnotationDto googleTextAnnotationDto = new GoogleTextAnnotationDto("id", "hi", googleBoundingPolyDto);

        GoogleVertex googleVertex = new GoogleVertex(50, 100);
        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(new ArrayList<>(Collections.singletonList(googleVertex)));
        GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation("id", "hi", googleBoundingPoly);

        //When
        GoogleTextAnnotation returnTextAnnotation = googleMapper.mapToGoogleTextAnnotation(googleTextAnnotationDto);

        //Then
        Assert.assertEquals(googleTextAnnotation, returnTextAnnotation);
    }

    @Test
    public void mapToGoogleResponses() {
        //Given
        GoogleVertexDto googleVertexDto = new GoogleVertexDto(50, 100);
        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Collections.singletonList(googleVertexDto)));
        GoogleTextAnnotationDto googleTextAnnotationDto = new GoogleTextAnnotationDto("id", "hi", googleBoundingPolyDto);
        GoogleResponsesDto googleResponsesDto = new GoogleResponsesDto(new ArrayList<>(Collections.singletonList(googleTextAnnotationDto)));

        GoogleVertex googleVertex = new GoogleVertex(50, 100);
        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(new ArrayList<>(Collections.singletonList(googleVertex)));
        GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation("id", "hi", googleBoundingPoly);
        GoogleResponses googleResponses = new GoogleResponses(new ArrayList<>(Collections.singletonList(googleTextAnnotation)));

        //When
        GoogleResponses returnResponses = googleMapper.mapToGoogleResponses(googleResponsesDto);

        //Then
        Assert.assertEquals(googleResponses, returnResponses);
    }

    @Test
    public void mapToGoogleResponse() {
        //Given
        GoogleVertexDto googleVertexDto = new GoogleVertexDto(50, 100);
        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Collections.singletonList(googleVertexDto)));
        GoogleTextAnnotationDto googleTextAnnotationDto = new GoogleTextAnnotationDto("id", "hi", googleBoundingPolyDto);
        GoogleResponsesDto googleResponsesDto = new GoogleResponsesDto(new ArrayList<>(Collections.singletonList(googleTextAnnotationDto)));
        GoogleResponseDto googleResponseDto = new GoogleResponseDto(new ArrayList<>(Collections.singletonList(googleResponsesDto)));

        GoogleVertex googleVertex = new GoogleVertex(50, 100);
        GoogleBoundingPoly googleBoundingPoly = new GoogleBoundingPoly(new ArrayList<>(Collections.singletonList(googleVertex)));
        GoogleTextAnnotation googleTextAnnotation = new GoogleTextAnnotation("id", "hi", googleBoundingPoly);
        GoogleResponses googleResponses = new GoogleResponses(new ArrayList<>(Collections.singletonList(googleTextAnnotation)));
        GoogleResponse googleResponse = new GoogleResponse(new ArrayList<>(Collections.singletonList(googleResponses)));

        //When
        GoogleResponse returnResponse = googleMapper.mapToGoogleResponse(googleResponseDto);

        //Then
        Assert.assertEquals(googleResponse, returnResponse);
    }
}