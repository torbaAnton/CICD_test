package com.example.springbootdocker.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckControllerTest {

    private static final String expectedResponse = "OK";

    @InjectMocks
    private HealthCheckController healthCheckController;

    @Test
    public void shouldReturnOk_whenHealthCheckEndpoint() {
        String actualResponse = healthCheckController.healthCheckEndpoint();
        assertEquals(expectedResponse, actualResponse);
    }
}
