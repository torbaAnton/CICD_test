package com.example.springbootdocker.dao;

import com.example.springbootdocker.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    String GET_PRODUCTS_ASSOCIATED_WITH_CATEGORY_SQL = "select p.* from products p inner join products_categories pc on p.id = pc.product_id where pc.category_id = :categoryId and (:name is null or p.name LIKE %:name%) and (:priceFrom is null or p.price > :priceFrom) and (:priceTo is null or p.price < :priceTo)";
    String COUNT_PRODUCTS_ASSOCIATED_WITH_CATEGORY_SQL = "select count(1) from (" + GET_PRODUCTS_ASSOCIATED_WITH_CATEGORY_SQL + ")";

    String GET_PRODUCTS_WITH_NAME_CONTAINING_AND_PRICE_IS_IN_RANGE_OPTIONALS = "select p.* from products p where (:name is null or p.name LIKE %:name%) and (:priceFrom is null or p.price > :priceFrom) and (:priceTo is null or p.price < :priceTo)";
    String COUNT_PRODUCTS_WITH_NAME_CONTAINING_AND_PRICE_IS_IN_RANGE_OPTIONALS = "select count(1) from (" + GET_PRODUCTS_WITH_NAME_CONTAINING_AND_PRICE_IS_IN_RANGE_OPTIONALS + ")";


    @Query(value = GET_PRODUCTS_ASSOCIATED_WITH_CATEGORY_SQL, countQuery = COUNT_PRODUCTS_ASSOCIATED_WITH_CATEGORY_SQL, nativeQuery = true)
    Page<ProductEntity> findByAssociatedWithCategory(@Param("categoryId") Long categoryId, @Param("name") String name, @Param("priceFrom") Long priceFrom, @Param("priceTo") Long priceTo, Pageable pageable);

    @Query(value = COUNT_PRODUCTS_ASSOCIATED_WITH_CATEGORY_SQL, nativeQuery = true)
    Integer countByAssociatedWithCategory(Long categoryId, String name, Long priceFrom, Long priceTo);

    @Query(value = GET_PRODUCTS_WITH_NAME_CONTAINING_AND_PRICE_IS_IN_RANGE_OPTIONALS, countQuery = COUNT_PRODUCTS_WITH_NAME_CONTAINING_AND_PRICE_IS_IN_RANGE_OPTIONALS, nativeQuery = true)
    Page<ProductEntity> findAll (@Param("name") String name, @Param("priceFrom") Long priceFrom, @Param("priceTo") Long priceTo, Pageable pageable);
}
