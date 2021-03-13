package com.example.springbootdocker.service.impl;

import com.example.springbootdocker.dao.CategoryRepository;
import com.example.springbootdocker.entity.CategoryEntity;
import com.example.springbootdocker.exception.CategoryNotFoundException;
import com.example.springbootdocker.mapper.CategoryToCategoryEntityMapper;
import com.example.springbootdocker.model.Category;
import com.example.springbootdocker.request.CategoryRequest;
import com.example.springbootdocker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryEntityMapper categoryToCategoryEntityMapper;

    @Transactional
    @Override
    public Page<Category> getAllCategories(Pageable page) {
        return categoryRepository.findAll(page).map(categoryToCategoryEntityMapper::mapToCategory);
    }

    @Transactional
    @Override
    public Category getCategoryById(Long id) {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);
        if (!categoryEntityOptional.isPresent()) {
            log.info("Category with id {} is not found.", id);
            throw new CategoryNotFoundException(id);
        }
        return categoryToCategoryEntityMapper.mapToCategory(categoryEntityOptional.get());
    }

    @Transactional
    @Override
    public Category createCategory(Category category) {
        category.setProductCount(0); // to remove
        CategoryEntity createdCategoryEntity = categoryRepository.save(categoryToCategoryEntityMapper.mapToCategoryEntity(category));
        Category createdCategory = categoryToCategoryEntityMapper.mapToCategory(createdCategoryEntity);
        log.info("Category with id {} is created.", createdCategory.getId());
        return createdCategory;
    }

    @Transactional
    @Override
    public Category updateCategory(Long categoryId, CategoryRequest categoryUpdateRequest) {
        Category categoryToUpdate = getCategoryById(categoryId);
        categoryToUpdate.setName(categoryUpdateRequest.getName());

        CategoryEntity updatedCategoryEntity = categoryRepository.save(categoryToCategoryEntityMapper.mapToCategoryEntity(categoryToUpdate));
        Category updatedCategory = categoryToCategoryEntityMapper.mapToCategory(updatedCategoryEntity);
        log.info("Category with id {} is updated.", updatedCategory.getId());
        return updatedCategory;
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        Category categoryToRemove = getCategoryById(categoryId);
        categoryRepository.delete(categoryToCategoryEntityMapper.mapToCategoryEntity(categoryToRemove));
    }

    @Transactional
    @Override
    public Category updateCategoryProductCount(Category category, Integer productCount) {
        category.setProductCount(productCount);
        return categoryToCategoryEntityMapper.mapToCategory(categoryRepository.save(categoryToCategoryEntityMapper.mapToCategoryEntity(category)));

    }
}
