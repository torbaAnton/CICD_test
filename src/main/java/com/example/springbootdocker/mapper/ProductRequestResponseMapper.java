package com.example.springbootdocker.mapper;

import com.example.springbootdocker.model.Product;
import com.example.springbootdocker.request.ProductRequest;
import com.example.springbootdocker.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductRequestResponseMapper {

    Product mapToProduct(ProductRequest source);

    @Mapping(target = "productId", source = "id")
    ProductResponse mapToProductResponse(Product source);
}
