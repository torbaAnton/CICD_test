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
class ProductControllerIT {

    private static final String CREATE_PRODUCT_REQUEST = TestHelperUtils.getFileContent("data/product/request/create-product.json", ProductControllerIT.class);
    private static final String UPDATE_PRODUCT_REQUEST = TestHelperUtils.getFileContent("data/product/request/update-product.json", ProductControllerIT.class);

    private static final String GET_PRODUCTS_RESPONSE = TestHelperUtils.getFileContent("data/product/response/get-products.json", ProductControllerIT.class);
    private static final String GET_PRODUCTS_FILTERED_BY_NAME_RESPONSE = TestHelperUtils.getFileContent("data/product/response/get-products-filtered-by-name.json", ProductControllerIT.class);
    private static final String GET_PRODUCTS_FILTERED_BY_PRICE_IN_RANGE_RESPONSE = TestHelperUtils.getFileContent("data/product/response/get-products-filtered-by-price-in-range.json", ProductControllerIT.class);
    private static final String GET_PRODUCTS_SORTED_BY_NAME_RESPONSE = TestHelperUtils.getFileContent("data/product/response/get-products-sorted-by-name.json", ProductControllerIT.class);
    private static final String GET_PRODUCTS_SORTED_BY_PRICE_RESPONSE = TestHelperUtils.getFileContent("data/product/response/get-products-sorted-by-price.json", ProductControllerIT.class);
    private static final String GET_PRODUCT_BY_ID_RESPONSE = TestHelperUtils.getFileContent("data/product/response/get-product-by-id.json", ProductControllerIT.class);
    private static final String CREATE_PRODUCT_RESPONSE = TestHelperUtils.getFileContent("data/product/response/create-product.json", ProductControllerIT.class);
    private static final String UPDATE_PRODUCT_RESPONSE = TestHelperUtils.getFileContent("data/product/response/update-product.json", ProductControllerIT.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private HttpEntity<String> httpEntity;

    @Test
    public void shouldRetrieveProductById() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/products/1"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCT_BY_ID_RESPONSE);
    }

    @Test
    public void shouldRetrieveProducts() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/products"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_RESPONSE);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldCreateProduct() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(CREATE_PRODUCT_REQUEST, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/products"), HttpMethod.POST, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse.getBody()).isEqualTo(CREATE_PRODUCT_RESPONSE);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldUpdateProduct() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(UPDATE_PRODUCT_REQUEST, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/products/1"), HttpMethod.PUT, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(UPDATE_PRODUCT_RESPONSE);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void shouldRemoveProduct() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/products/1"), HttpMethod.DELETE, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(actualResponse.getBody()).isEqualTo(null);
    }

    @Test
    public void shouldRetrieveProductsFilteredByNameContaining() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/products?name=mil"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_FILTERED_BY_NAME_RESPONSE);
    }

    @Test
    public void shouldRetrieveProductsFilteredByPriceInRange() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/products?priceFrom=1&priceTo=3"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_FILTERED_BY_PRICE_IN_RANGE_RESPONSE);
    }

    @Test
    public void shouldRetrieveProductsSortedByName() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/products?sortBy=name"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_SORTED_BY_NAME_RESPONSE);
    }

    @Test
    public void shouldRetrieveProductsSortedByPrice() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/products?sortBy=price"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_SORTED_BY_PRICE_RESPONSE);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}