package com.example.springbootdocker.response;

import com.example.springbootdocker.entity.CategoryEntity;
import com.example.springbootdocker.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {
    private long productId;
    private BigDecimal price;
    private String name;
    private List<Category> categories;
}
