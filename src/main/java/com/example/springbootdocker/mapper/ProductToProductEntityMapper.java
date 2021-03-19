package com.example.springbootdocker.mapper;

import com.example.springbootdocker.entity.ProductEntity;
import com.example.springbootdocker.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductToProductEntityMapper {
    ProductEntity mapToProductEntity(Product source);

    Product mapToProduct(ProductEntity source);
}



