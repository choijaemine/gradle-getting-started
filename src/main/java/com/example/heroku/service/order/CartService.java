package com.example.heroku.service.order;

import com.example.heroku.controller.order.request.AddCartRequest;
import com.example.heroku.controller.order.request.ChangeItemCountRequest;
import com.example.heroku.controller.order.request.SelectCartItemRequest;
import com.example.heroku.entity.order.Item;

import java.util.List;

public interface CartService {
    //public void addCartItem(AddCartRequest addCartRequest);
    public void deleteCartItem(SelectCartItemRequest selectCartItemRequest);


    List<Item> returnCartItemList(String userToken);
    List<Item> deleteCartItem(long itemNo, String token);
    String changeCartItemCount (ChangeItemCountRequest changeItemCountRequest);
    String addProductInCart(AddCartRequest addCartRequest);
    Item isExistItemCheck(AddCartRequest addCartRequest, long id);


}
