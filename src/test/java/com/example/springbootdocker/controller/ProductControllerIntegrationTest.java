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

@SpringBootTest(classes = SpringBootDockerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:test_data.sql"})
class ProductControllerIntegrationTest {

    private final static String CREATE_PRODUCT_REQUEST = getFileContent("data/product/request/create-product.json");
    private final static String UPDATE_PRODUCT_REQUEST = getFileContent("data/product/request/update-product.json");

    private final static String GET_PRODUCTS_RESPONSE = getFileContent("data/product/response/get-products.json");
    private final static String GET_PRODUCT_BY_ID_RESPONSE = getFileContent("data/product/response/get-product-by-id.json");
    private final static String CREATE_PRODUCT_RESPONSE = getFileContent("data/product/response/create-product.json");
    private final static String UPDATE_PRODUCT_RESPONSE = getFileContent("data/product/response/update-product.json");

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
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCT_BY_ID_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldRetrieveOrders() {
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/products"), HttpMethod.GET, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldCreateProduct() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(CREATE_PRODUCT_REQUEST.replaceAll("[\\s+\\r\\n]", ""), httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/products"), HttpMethod.POST, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse.getBody()).isEqualTo(CREATE_PRODUCT_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldUpdateProduct() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(UPDATE_PRODUCT_REQUEST.replaceAll("[\\s+\\r\\n]", ""), httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/products/1"), HttpMethod.PUT, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(UPDATE_PRODUCT_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldRemoveProduct() {
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/products/1"), HttpMethod.DELETE, httpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(actualResponse.getBody()).isEqualTo(null);
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