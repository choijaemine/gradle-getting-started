package com.example.heroku.controller.products.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class ProductLocalAndTypeRequest {
    private String alcoholType;
    private String localName;


}
