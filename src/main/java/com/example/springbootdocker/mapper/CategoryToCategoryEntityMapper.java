package com.example.springbootdocker.mapper;

import com.example.springbootdocker.entity.CategoryEntity;
import com.example.springbootdocker.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryToCategoryEntityMapper {
    CategoryEntity mapToCategoryEntity(Category source);

    Category mapToCategory(CategoryEntity source);
}
