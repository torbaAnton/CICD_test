package com.example.springbootdocker.service;

import com.example.springbootdocker.model.Category;
import com.example.springbootdocker.request.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<Category> getAllCategories(Pageable page);

    Category getCategoryById(Long id);

    Category createCategory(Category category);

    Category updateCategory(Long categoryId, CategoryRequest categoryRequest);

    void deleteCategory(Long categoryId);

    Category updateCategoryProductCount(Category categor, Integer productCount);

}
