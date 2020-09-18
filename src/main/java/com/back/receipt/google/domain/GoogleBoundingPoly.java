package com.back.receipt.google.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GoogleBoundingPoly {

    private List<GoogleVertex> vertices = null;

}
