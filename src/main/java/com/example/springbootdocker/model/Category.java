package com.example.springbootdocker.model;

import com.example.springbootdocker.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Category {

    private Long id;
    private Integer productCount;
    private String name;
    private List<ProductEntity> products;

}
