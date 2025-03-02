package com.example.heroku.entity.products;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AlcoholType {

    SOJU_SPIRITS("소주증류주"), LIQUEUR("리큐르"), MAKGEOLLI("막걸리"), YAKJU_CHEONGJU("약주청주"), FRUITWINE("과실주"), OTHER("기타주류");

    final private String type;

    public static AlcoholType valueOfAlcoholName(String alcohol) {
        return Arrays.stream(values())
                .filter(value -> value.type.equals(alcohol))
                .findAny()
                .orElse(null);
    }

}
