package com.back.receipt.google;

import com.back.receipt.google.domain.dto.GoogleResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObject;
import java.net.URI;

@Component
public class GoogleClient {

    @Resource
    private GoogleConfig googleConfig;

    @Autowired
    private RestTemplate restTemplate;

    public GoogleResponseDto getGoogleDto(String content) {

        JsonObject requestJson = createRequest(content);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);

        URI uri = getURI();

        return restTemplate.postForObject(uri, entity, GoogleResponseDto.class);
    }

    private JsonObject createRequest(String content) {
        return Json.createObjectBuilder()
                .add("requests", Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                        .add("image", Json.createObjectBuilder()
                            .add("content", content)
                        )
                        .add("features", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder()
                                .add("type", "DOCUMENT_TEXT_DETECTION")
                            )
                        )
                    )
                ).build();
    }

    private URI getURI() {
        return UriComponentsBuilder.fromHttpUrl(googleConfig.getGoogleApiEndpoint())
                .queryParam("key", googleConfig.getGoogleAppKey())
                .build().encode().toUri();
    }
}
