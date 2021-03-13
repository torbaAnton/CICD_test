package com.example.springbootdocker.service;

import com.example.springbootdocker.model.Category;
import com.example.springbootdocker.model.Product;
import com.example.springbootdocker.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<Product> getAllProducts(String name, Long priceFrom, Long priceTo, Pageable page);

    Page<Product> getAllProducts(Long categoryId, String name, Long priceFrom, Long priceTo, Pageable page);

    Product getProductById(Long id);

    Product createProduct(Product product);

    Product updateProduct(Long productId, ProductRequest productRequest);

    void deleteProduct(Long productId);

    boolean hasCategory(Product product, Category category);

    void addCategory(Long productId, Long categoryId);

    void removeCategory(Long productId, Long categoryId);

    Integer countProductsAssociated (Category category, String name, Long priceFrom, Long priceTo);
}
