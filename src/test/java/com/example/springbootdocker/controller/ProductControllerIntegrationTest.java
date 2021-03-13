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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SpringBootDockerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

    private final static String GET_PRODUCTS_RESPONSE = getFileContent("data/product/response/get-products.json");
    private final static String GET_PRODUCT_BY_ID_RESPONSE = getFileContent("data/product/response/get-product-by-id.json");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    HttpEntity<String> emptyHttpEntity = new HttpEntity<>(null, new HttpHeaders());

    @Test
    public void shouldRetrieveProductById() {
        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("api/products/1"), HttpMethod.GET, emptyHttpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCT_BY_ID_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
    }

    @Test
    public void shouldRetrieveOrders() {
        ResponseEntity<String> actualResponse = this.restTemplate
                .exchange(createURLWithPort("/api/products"), HttpMethod.GET, emptyHttpEntity, String.class);
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(GET_PRODUCTS_RESPONSE.replaceAll("[\\s+\\r\\n]", ""));
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