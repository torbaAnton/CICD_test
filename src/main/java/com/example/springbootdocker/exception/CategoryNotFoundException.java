package com.example.springbootdocker.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Long categoryId;

    public CategoryNotFoundException(Long id) {
        super("Category not found " + id);
        this.categoryId = id;
    }
}
