package com.example.heroku.service.products;

import com.example.heroku.controller.products.response.FavoriteResponse;
import com.example.heroku.entity.products.Favorite;

import java.util.List;

public interface FavoriteService {
    public FavoriteResponse favoriteStatus(Long memberId, Long productNo, String favoriteType);

    public List<Favorite> myFavoriteProductList(String token);



}
