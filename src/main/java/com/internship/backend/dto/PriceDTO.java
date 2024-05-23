package com.internship.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {
    private int id;
    private String season;
    private String periodOfDay;
    private int price;
    private int tennisCourtId;
}
