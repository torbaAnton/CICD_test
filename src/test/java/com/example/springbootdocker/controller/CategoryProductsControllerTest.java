package com.example.springbootdocker.controller;

import com.example.springbootdocker.SpringBootDockerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Initial data inserted to db through resources/test_data.sql script
 */

@SpringBootTest(classes = SpringBootDockerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:test_data.sql"})
class CategoryProductsControllerTest {

    private final static String GET_PRODUCTS_BY_CATEGORY_ID_RESPONSE = getFileContent("data/category_products/response/get-products-by-category-id.json");
    private final static String GET_PRODUCTS_BY_CATEGORY_ID_FILTERED_BY_NAME_RESPONSE = getFileContent("data/category_products/response/get-products-by-category-id-filtered-by-name.json");
    private final static String GET_PRODUCTS_BY_CATEGORY_ID_FILTERED_BY_PRICE_IN_RANGE_RESPONSE = getFileContent("data/category_products/response/get-products-by-category-id-filtered-by-price-in-range.json");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private HttpEntity<String> httpEntity;


    @Test
    public void shouldRetrieveProductsByCategoryId() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories/1/products"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_BY_CATEGORY_ID_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldAssignCategoryToProduct() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse.getBody()).isEqualTo(null);
    }

    @Test
    public void shouldUnassignCategoryFromProduct() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.DELETE, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(actualResponse.getBody()).isEqualTo(null);
    }

    @Test
    public void shouldRetrieveProductsByCategoryIdFilteredByNameContaining() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);
        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/2"), HttpMethod.POST, httpEntity, String.class);
        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/3"), HttpMethod.POST, httpEntity, String.class);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories/1/products?name=mea"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_BY_CATEGORY_ID_FILTERED_BY_NAME_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldRetrieveProductsByCategoryIdFilteredByPriceInRange() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/1"), HttpMethod.POST, httpEntity, String.class);
        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/2"), HttpMethod.POST, httpEntity, String.class);
        this.restTemplate.exchange(createURLWithPort("api/categories/1/products/3"), HttpMethod.POST, httpEntity, String.class);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1/products?priceFrom=1&priceTo=3"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_BY_CATEGORY_ID_FILTERED_BY_PRICE_IN_RANGE_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private static String getFileContent(String filePath) {
        ClassPathResource resource =
                new ClassPathResource(filePath, ProductControllerIntegrationTest.class.getClassLoader());
        try {
            return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read the expected data from json file!", e);
        }
    }
}