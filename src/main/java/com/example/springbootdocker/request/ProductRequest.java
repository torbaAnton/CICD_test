package com.example.springbootdocker.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotEmpty(message = "name is required")
    @Size(message = "name must be equal to or lower than 100", min = 1, max = 100)
    private String name;
    @NotEmpty(message = "price is required")
    @Min(0)
    private BigDecimal price;
    @NotEmpty
    @Size(message = "Currency must be in ISO 4217 format", min = 3, max = 3)
    private String currency;
}
