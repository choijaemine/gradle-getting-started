package com.example.heroku.entity.products;

import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import com.example.heroku.utility.product.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    String thumbnailFileName;

    @Convert(converter = StringListConverter.class)
    List<String> productImagesName;

    @Convert(converter = StringListConverter.class)
    List<String> taste;

    String subTitle;

    String description;

}
