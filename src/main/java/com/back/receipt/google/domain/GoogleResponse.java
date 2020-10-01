package com.back.receipt.google.domain;

import com.back.receipt.google.domain.dto.GoogleResponsesDto;
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
public class GoogleResponse {

    private List<GoogleResponses> googleResponsesDtoList = new ArrayList<>();

}
