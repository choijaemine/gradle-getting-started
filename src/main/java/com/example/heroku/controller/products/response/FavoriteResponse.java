package com.example.heroku.controller.products.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FavoriteResponse {
    private Integer favoriteNum;
    private Boolean productFavoriteFlag;
}
