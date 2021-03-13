package com.example.springbootdocker.model;

import com.example.springbootdocker.entity.CategoryEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {

    private Long id;
    private BigDecimal price;
    private String name;
    private List<CategoryEntity> categories;
}
