package com.back.receipt.google.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Setter
public class GoogleResponseDto {

    @JsonProperty("responses")
    private List<GoogleResponsesDto> googleResponsesDtoList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleResponseDto that = (GoogleResponseDto) o;

        return googleResponsesDtoList != null ? googleResponsesDtoList.equals(that.googleResponsesDtoList) : that.googleResponsesDtoList == null;
    }

    @Override
    public int hashCode() {
        return googleResponsesDtoList != null ? googleResponsesDtoList.hashCode() : 0;
    }
}
