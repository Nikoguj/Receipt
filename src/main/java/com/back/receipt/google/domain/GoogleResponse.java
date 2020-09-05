package com.back.receipt.google.domain;

import com.back.receipt.google.domain.dto.GoogleResponsesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleResponse {

    private List<GoogleResponses> googleResponsesDtoList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoogleResponse that = (GoogleResponse) o;

        return googleResponsesDtoList != null ? googleResponsesDtoList.equals(that.googleResponsesDtoList) : that.googleResponsesDtoList == null;
    }

    @Override
    public int hashCode() {
        return googleResponsesDtoList != null ? googleResponsesDtoList.hashCode() : 0;
    }
}
