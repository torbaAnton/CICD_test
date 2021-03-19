package com.example.springbootdocker.controller;

import com.example.springbootdocker.mapper.CategoryRequestResponseMapper;
import com.example.springbootdocker.model.Category;
import com.example.springbootdocker.response.PageResponse;
import com.example.springbootdocker.request.CategoryRequest;
import com.example.springbootdocker.response.CategoryResponse;
import com.example.springbootdocker.service.CategoryService;
import com.example.springbootdocker.utils.PageResponseUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRequestResponseMapper categoryRequestResponseMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new category.", response = CategoryResponse.class)
    public CategoryResponse create(@RequestBody CategoryRequest request) {
        log.debug("POST /categories - Add a new category");
        Category category = categoryRequestResponseMapper.mapToCategory(request);
        Category createdCategory = categoryService.createCategory(category);
        return categoryRequestResponseMapper.mapToCategoryResponse(createdCategory);
    }

    @GetMapping(path = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a category by category ID.", response = Category.class)
    public Category getCategory(@PathVariable Long categoryId) {
        log.debug("GET /categories/{categoryId} - Get a category by category ID");
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get categories.", response = Category.class)
    public PageResponse<Category> getCategories(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                @RequestParam(value = "direction", defaultValue = "asc", required = false) String direction,
                                                @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy) {
        log.debug("GET /categories - Get categories");
        PageRequest pageRequest
                = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Category> categoryPage = categoryService.getAllCategories(pageRequest);

        return PageResponseUtils.getPageResponse(direction, sortBy, categoryPage);
    }

    @PutMapping(path = "/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update category by category ID.", response = Category.class)
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest request) {
        log.debug("PUT /categories/{categoryId} - Update a category by category ID");

        return categoryService.updateCategory(categoryId, request);
    }

    @DeleteMapping(path = "/{categoryId}")
    @ApiOperation(value = "Remove category by category ID.", response = ResponseEntity.class)
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        log.debug("DELETE /categories/{categoryId} - Delete a category by category ID");

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }
}
