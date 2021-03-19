package com.example.springbootdocker.service.impl;

import com.example.springbootdocker.dao.ProductRepository;
import com.example.springbootdocker.entity.ProductEntity;
import com.example.springbootdocker.exception.CategoryNotFoundException;
import com.example.springbootdocker.exception.ProductNotFoundException;
import com.example.springbootdocker.mapper.CategoryToCategoryEntityMapper;
import com.example.springbootdocker.mapper.ProductToProductEntityMapper;
import com.example.springbootdocker.model.Category;
import com.example.springbootdocker.model.Product;
import com.example.springbootdocker.request.ProductRequest;
import com.example.springbootdocker.service.CategoryService;
import com.example.springbootdocker.service.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductToProductEntityMapper productToProductEntityMapper;
    private final CategoryToCategoryEntityMapper categoryToCategoryEntityMapper;
    private final CategoryService categoryService;


    @Transactional
    @Override
    public Page<Product> getAllProducts(String name, Long priceFrom, Long priceTo, Pageable page) {
        return productRepository.findAll(name, priceFrom, priceTo, page).map(productToProductEntityMapper::mapToProduct);
    }

    @Transactional
    @Override
    public Page<Product> getAllProducts(Long categoryId, String name, Long priceFrom, Long priceTo, Pageable page) {
        final Category category = categoryService.getCategoryById(categoryId);
        return productRepository.findByAssociatedWithCategory(category.getId(), name, priceFrom, priceTo, page).map(productToProductEntityMapper::mapToProduct);
    }

    @Transactional
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).map(productToProductEntityMapper::mapToProduct).orElseThrow(() -> {
            log.info("Product with id {} is not found.", id);
            return new ProductNotFoundException(id);
        });
    }

    @Transactional
    @Override
    public Product createProduct(Product product) {
        ProductEntity createdProductEntity = productRepository.save(productToProductEntityMapper.mapToProductEntity(product));
        Product createdProduct = productToProductEntityMapper.mapToProduct(createdProductEntity);
        log.info("Product with id {} is created.", createdProduct.getId());
        return createdProduct;
    }

    @Transactional
    @Override
    public Product updateProduct(Long productId, ProductRequest productUpdateRequest) {
        Product productToUpdate = getProductById(productId);
        productToUpdate.setName(productUpdateRequest.getName());
        productToUpdate.setPrice(productUpdateRequest.getPrice());

        ProductEntity updatedProductEntity = productRepository.save(productToProductEntityMapper.mapToProductEntity(productToUpdate));
        Product updatedProduct = productToProductEntityMapper.mapToProduct(updatedProductEntity);
        log.info("Product with id {} is updated.", updatedProduct.getId());
        return updatedProduct;
    }

    @Transactional
    @Override
    public void deleteProduct(Long productId) {
        Product productToRemove = getProductById(productId);
        productRepository.delete(productToProductEntityMapper.mapToProductEntity(productToRemove));
    }

    @Transactional
    @Override
    public boolean hasCategory(Product product, Category category) {
        return product.getCategories().contains(categoryToCategoryEntityMapper.mapToCategoryEntity(category));
    }

    @Transactional
    @Override
    public void addCategory(Long productId, Long categoryId) {
        final Category category = categoryService.getCategoryById(categoryId);
        final Product product = getProductById(productId);

        if (hasCategory(product, category)) {
            throw new IllegalArgumentException("product " + product.getId() + " already contains category " + category.getId());
        }
        product.getCategories().add(categoryToCategoryEntityMapper.mapToCategoryEntity(category));
        productRepository.save(productToProductEntityMapper.mapToProductEntity(product));
        categoryService.updateCategoryProductCount(category, countProductsAssociated(category, null, null, null));
    }

    @Transactional
    @Override
    public void removeCategory(Long productId, Long categoryId) {
        final Category category = categoryService.getCategoryById(categoryId);
        final Product product = getProductById(productId);

        if (!hasCategory(product, category)) {
            throw new IllegalArgumentException("product " + product.getId() + " does not contain category " + category.getId());
        }
        product.getCategories().remove(categoryToCategoryEntityMapper.mapToCategoryEntity(category));
        productRepository.save(productToProductEntityMapper.mapToProductEntity(product));
        categoryService.updateCategoryProductCount(category, countProductsAssociated(category, null, null, null));
    }

    @Transactional
    @Override
    public Integer countProductsAssociated(Category category, String name, Long priceFrom, Long priceTo) {
        return productRepository.countByAssociatedWithCategory(category.getId(), name, priceFrom, priceTo);
    }
}
