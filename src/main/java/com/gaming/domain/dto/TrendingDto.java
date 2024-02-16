package com.gaming.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrendingDto {
    private String term;
    private Integer count;

    public TrendingDto(String term, Integer count){
        this.term = term;
        this.count = count;
    }
}
