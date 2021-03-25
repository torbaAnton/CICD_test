package com.example.springbootdocker.controller;

import com.example.springbootdocker.SpringBootDockerApplication;
import com.example.springbootdocker.utils.TestHelperUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Initial data inserted to db through resources/test_data.sql script
 */

@SpringBootTest(classes = SpringBootDockerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoryProductsControllerTest {

    private static final String GET_PRODUCTS_BY_CATEGORY_ID_RESPONSE = TestHelperUtils.getFileContent("data/category_products/response/get-products-by-category-id.json", CategoryProductsControllerTest.class);
    private static final String GET_PRODUCTS_BY_CATEGORY_ID_FILTERED_BY_NAME_RESPONSE = TestHelperUtils.getFileContent("data/category_products/response/get-products-by-category-id-filtered-by-name.json", CategoryProductsControllerTest.class);
    private static final String GET_PRODUCTS_BY_CATEGORY_ID_FILTERED_BY_PRICE_IN_RANGE_RESPONSE = TestHelperUtils.getFileContent("data/category_products/response/get-products-by-category-id-filtered-by-price-in-range.json", CategoryProductsControllerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private HttpEntity<String> httpEntity;


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldRetrieveProductsByCategoryId() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories/1/products"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_BY_CATEGORY_ID_RESPONSE);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldAssignCategoryToProduct() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse.getBody()).isEqualTo(null);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldUnassignCategoryFromProduct() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.DELETE, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(actualResponse.getBody()).isEqualTo(null);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldRetrieveProductsByCategoryIdFilteredByNameContaining() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);
        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/2"), HttpMethod.POST, httpEntity, String.class);
        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/3"), HttpMethod.POST, httpEntity, String.class);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories/1/products?name=mea"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_BY_CATEGORY_ID_FILTERED_BY_NAME_RESPONSE);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldRetrieveProductsByCategoryIdFilteredByPriceInRange() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);
        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/2"), HttpMethod.POST, httpEntity, String.class);
        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/3"), HttpMethod.POST, httpEntity, String.class);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1/products?priceFrom=1&priceTo=3"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_BY_CATEGORY_ID_FILTERED_BY_PRICE_IN_RANGE_RESPONSE);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }


}