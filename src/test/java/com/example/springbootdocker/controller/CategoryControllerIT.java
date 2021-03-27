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
 * Initial data inserted to db through migration init data scripts
 */

@SpringBootTest(classes = SpringBootDockerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoryControllerIT {

    private static final String CREATE_CATEGORY_REQUEST = TestHelperUtils.getFileContent("data/category/request/create-category.json", CategoryControllerIT.class);
    private static final String UPDATE_CATEGORY_REQUEST = TestHelperUtils.getFileContent("data/category/request/update-category.json", CategoryControllerIT.class);

    private static final String GET_CATEGORIES_RESPONSE = TestHelperUtils.getFileContent("data/category/response/get-categories.json", CategoryControllerIT.class);
    private static final String GET_CATEGORIES_SORTED_BY_NAME_RESPONSE = TestHelperUtils.getFileContent("data/category/response/get-categories-sorted-by-name.json", CategoryControllerIT.class);
    private static final String GET_CATEGORIES_SORTED_BY_PRODUCT_COUNT_RESPONSE = TestHelperUtils.getFileContent("data/category/response/get-categories-sorted-by-productCount.json", CategoryControllerIT.class);
    private static final String GET_CATEGORY_BY_ID_RESPONSE = TestHelperUtils.getFileContent("data/category/response/get-category-by-id.json", CategoryControllerIT.class);
    private static final String CREATE_CATEGORY_RESPONSE = TestHelperUtils.getFileContent("data/category/response/create-category.json", CategoryControllerIT.class);
    private static final String UPDATE_CATEGORY_RESPONSE = TestHelperUtils.getFileContent("data/category/response/update-category.json", CategoryControllerIT.class);

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
        assertThat(actualResponse.getBody()).isEqualTo(GET_CATEGORY_BY_ID_RESPONSE);
    }

    @Test
    public void shouldRetrieveCategories() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_CATEGORIES_RESPONSE);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldCreateCategory() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(CREATE_CATEGORY_REQUEST, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories"), HttpMethod.POST, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse.getBody()).isEqualTo(CREATE_CATEGORY_RESPONSE);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldUpdateCategory() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(UPDATE_CATEGORY_REQUEST, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/categories/1"), HttpMethod.PUT, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(UPDATE_CATEGORY_RESPONSE);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        assertThat(actualResponse.getBody()).isEqualTo(GET_CATEGORIES_SORTED_BY_NAME_RESPONSE);
    }

    @Test
    public void shouldRetrieveCategoriesSortedByProductCount() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/categories?sortBy=productCount"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_CATEGORIES_SORTED_BY_PRODUCT_COUNT_RESPONSE);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}