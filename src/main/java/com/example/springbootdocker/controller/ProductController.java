package com.example.springbootdocker.controller;

import com.example.springbootdocker.mapper.ProductRequestResponseMapper;
import com.example.springbootdocker.model.Product;
import com.example.springbootdocker.request.ProductRequest;
import com.example.springbootdocker.response.PageResponse;
import com.example.springbootdocker.response.ProductResponse;
import com.example.springbootdocker.service.ProductService;
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
@RequestMapping(path = "/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductRequestResponseMapper productRequestResponseMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new product.", response = ProductResponse.class)
    public ProductResponse create(@RequestBody ProductRequest request) {
        log.debug("POST /products - Add a new product");
        Product product = productRequestResponseMapper.mapToProduct(request);
        Product createdProduct = productService.createProduct(product);
        return productRequestResponseMapper.mapToProductResponse(createdProduct);
    }

    @GetMapping(path = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a product by product ID.", notes = "Get a product by product ID.", response = Product.class)
    public Product getProduct(@PathVariable Long productId) {
        log.debug("GET /products/{productId} - Get a product by product ID");
        return productService.getProductById(productId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get products.", response = Product.class)
    public PageResponse<Product> getProducts(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                             @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                             @RequestParam(value = "direction", defaultValue = "asc", required = false) String direction,
                                             @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "priceFrom", required = false) Long priceFrom,
                                             @RequestParam(value = "priceTo", required = false) Long priceTo) {
        log.debug("GET /products - Get products");
        PageRequest pageRequest
                = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<Product> productPage = productService.getAllProducts(name, priceFrom, priceTo, pageRequest);

        return PageResponseUtils.getProductPageResponse(direction, sortBy, productPage);
    }

    @PutMapping(path = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update  product by product ID.", response = Product.class)
    public Product updateProduct(@PathVariable Long productId, @RequestBody ProductRequest request) {
        log.debug("PUT /products/{productId} - Delete a product by product ID");

        return productService.updateProduct(productId, request);
    }

    @DeleteMapping(path = "/{productId}")
    @ApiOperation(value = "Remove  product by product ID.", response = Product.class)
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        log.debug("DELETE /products/{productId} - Delete a product by product ID");

        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }
}
