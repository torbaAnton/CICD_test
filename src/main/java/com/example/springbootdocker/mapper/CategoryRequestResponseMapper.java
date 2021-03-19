package com.example.springbootdocker.mapper;

import com.example.springbootdocker.model.Category;
import com.example.springbootdocker.request.CategoryRequest;
import com.example.springbootdocker.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryRequestResponseMapper {
    Category mapToCategory(CategoryRequest source);

    @Mapping(target = "categoryId", source = "id")
    CategoryResponse mapToCategoryResponse(Category source);
}
