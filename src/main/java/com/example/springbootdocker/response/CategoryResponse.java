package com.example.springbootdocker.response;

import com.example.springbootdocker.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryResponse {
    private long categoryId;
    private Integer productCount;
    private String name;
    private List<ProductEntity> products;
}
