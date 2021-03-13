package com.example.springbootdocker.controller;

import com.example.springbootdocker.model.Category;
import com.example.springbootdocker.model.Product;
import com.example.springbootdocker.response.PageResponse;
import com.example.springbootdocker.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/categories/{categoryId}/products")
public class CategoryProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get products by category ID.", response = Product.class)
    public PageResponse<Product> retrieveAllProductsInCategory(@PathVariable Long categoryId, @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                             @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                             @RequestParam(value = "direction", defaultValue = "asc", required = false) String direction,
                                             @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                               @RequestParam(value = "name", required = false) String name,
                                                               @RequestParam(value = "priceFrom", required = false) Long priceFrom,
                                                               @RequestParam(value = "priceTo", required = false) Long priceTo) {
        log.debug("get /categories/{categoryId}/products - Get products in category by category ID");

        PageRequest pageRequest
                = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Product> productsInCategoryPage = productService.getAllProducts(categoryId, name, priceFrom, priceTo, pageRequest);
        return PageResponse.<Product>builder()
                .content(productsInCategoryPage.getContent())
                .size(productsInCategoryPage.getSize())
                .page(productsInCategoryPage.getNumber())
                .sortBy(sortBy)
                .direction(direction)
                .totalElements(productsInCategoryPage.getTotalElements())
                .totalPages(productsInCategoryPage.getTotalPages())
                .build();
    }

    @PostMapping(path = "/{productId}")
    @ApiOperation(value = "Add category to product by product ID and by category ID.", response = ResponseEntity.class)
    public ResponseEntity<?> addCategoryToProduct(@PathVariable Long categoryId, @PathVariable Long productId) {
        log.debug("UPDATE /categories/{categoryId}/products/{productId} - Add a category to product by product ID and by category ID");

        productService.addCategory(productId, categoryId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{productId}")
    @ApiOperation(value = "Remove category from product by product ID and by category ID.", response = ResponseEntity.class)
    public ResponseEntity<?> removeProduct(@PathVariable Long categoryId, @PathVariable Long productId) {
        log.debug("DELETE /categories/{categoryId}/products/{productId} - Delete a category from product by product ID and by category ID");

        productService.removeCategory(productId, categoryId);

        return ResponseEntity.noContent().build();
    }
}