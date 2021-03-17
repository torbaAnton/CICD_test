package com.example.springbootdocker.utils;

import com.example.springbootdocker.model.Category;
import com.example.springbootdocker.model.Product;
import com.example.springbootdocker.response.PageResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class PageResponseUtils {

    public PageResponse<Category> getCategoryPageResponse(String direction, String sortBy, Page<Category> categoryPage) {
        return PageResponse.<Category>builder()
                .content(categoryPage.getContent())
                .size(categoryPage.getSize())
                .page(categoryPage.getNumber())
                .sortBy(sortBy)
                .direction(direction)
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .build();
    }

    public PageResponse<Product> getProductPageResponse(String direction, String sortBy, Page<Product> productPage) {
        return PageResponse.<Product>builder()
                .content(productPage.getContent())
                .size(productPage.getSize())
                .page(productPage.getNumber())
                .sortBy(sortBy)
                .direction(direction)
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }
}

