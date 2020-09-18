package com.back.receipt.google;

import com.back.receipt.google.domain.dto.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleClientTest {

    @Autowired
    private GoogleClient googleClient;

    private static String imageInBase64 = "iVBORw0KGgoAAAANSUhEUgAAACMAAAAvCAYAAABpG3MiAAAAAXNSR0IArs4c6QAAAARnQU" +
            "1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAADiSURBVFhH7ZhBDsIgEEUHz1JdGE8w3IcuvQoewUu0F3FlexekFU2s/V" +
            "pIF138l0zoFBJeOtAQTIjIRtildhNQBkEZBGUQlEEskmlrI8Y8w1769HZ9WCYEZRCUQZTL9K1cais2bfkxbMzrVvrS3T+c9P7ROB" +
            "lOg2Oo70Ln9Z3Ph4Y4LJtsmemkqi44je20T33I9SmU0eCa76mmX8w1qWMhBTK/S/AxNtMmewGrv8q5SskM+2Ms2IvbXXLW8upbuz" +
            "qc0lM+/OkhKIOgDIIyCMogKIPgnR6CMgjKICiDoAxiQzIiD4UOzZo3mxprAAAAAElFTkSuQmCC";

    @Test
    public void getGoogleDto() {
        //Given
        GoogleVertexDto vertexDto1 = new GoogleVertexDto(9, 9);
        GoogleVertexDto vertexDto2 = new GoogleVertexDto(23, 9);
        GoogleVertexDto vertexDto3 = new GoogleVertexDto(23, 37);
        GoogleVertexDto vertexDto4 = new GoogleVertexDto(9, 37);

        GoogleBoundingPolyDto googleBoundingPolyDto = new GoogleBoundingPolyDto(new ArrayList<>(Arrays.asList(
                vertexDto1, vertexDto2, vertexDto3, vertexDto4
        )));

        GoogleTextAnnotationDto googleTextAnnotationDto1 = new GoogleTextAnnotationDto("und", "h\n", googleBoundingPolyDto);
        GoogleTextAnnotationDto googleTextAnnotationDto2 = new GoogleTextAnnotationDto(null, "h", googleBoundingPolyDto);

        GoogleResponsesDto googleResponsesDto = new GoogleResponsesDto(new ArrayList<>(Arrays.asList(
                googleTextAnnotationDto1, googleTextAnnotationDto2
        )));

        GoogleResponseDto googleResponseDto = new GoogleResponseDto(new ArrayList<>(Arrays.asList(
                googleResponsesDto
        )));

        //When
        GoogleResponseDto returnGoogleDto = googleClient.getGoogleDto(imageInBase64);

        //Then
        Assert.assertEquals(returnGoogleDto, googleResponseDto);
    }
}