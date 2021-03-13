package com.example.springbootdocker.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Long productId;

    public ProductNotFoundException(Long id) {
        super("Product not found " + id);
        this.productId = id;
    }
}