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
class CategoryControllerIntegrationTest {

    private final static String CREATE_CATEGORY_REQUEST = getFileContent("data/category/request/create-category.json");
    private final static String UPDATE_CATEGORY_REQUEST = getFileContent("data/category/request/update-category.json");

    private final static String GET_CATEGORIES_RESPONSE = getFileContent("data/category/response/get-categories.json");
    private final static String GET_CATEGORIES_SORTED_BY_NAME_RESPONSE = getFileContent("data/category/response/get-categories-sorted-by-name.json");
    private final static String GET_CATEGORIES_SORTED_BY_PRODUCT_COUNT_RESPONSE = getFileContent("data/category/response/get-categories-sorted-by-productCount.json");
    private final static String GET_CATEGORY_BY_ID_RESPONSE = getFileContent("data/category/response/get-category-by-id.json");
    private final static String CREATE_CATEGORY_RESPONSE = getFileContent("data/category/response/create-category.json");
    private final static String UPDATE_CATEGORY_RESPONSE = getFileContent("data/category/response/update-category.json");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private HttpEntity<String> httpEntity;

    @Test
    public void shouldRetrieveCategoryById() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_CATEGORY_BY_ID_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldRetrieveCategories() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_CATEGORIES_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldCreateCategory() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(CREATE_CATEGORY_REQUEST.replaceAll("[\\s+\\r\\n]", ""), httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories"), HttpMethod.POST, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse.getBody()).isEqualTo(CREATE_CATEGORY_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldUpdateCategory() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(UPDATE_CATEGORY_REQUEST.replaceAll("[\\s+\\r\\n]", ""), httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1"), HttpMethod.PUT, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(UPDATE_CATEGORY_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldRemoveCategory() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1"), HttpMethod.DELETE, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(actualResponse.getBody()).isEqualTo(null);
    }

    @Test
    public void shouldRetrieveCategoriesSortedByName() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories?sortBy=name"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_CATEGORIES_SORTED_BY_NAME_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldRetrieveCategoriesSortedByProductCount() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories?sortBy=productCount"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_CATEGORIES_SORTED_BY_PRODUCT_COUNT_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
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