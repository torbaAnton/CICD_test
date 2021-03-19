package com.example.springbootdocker.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotEmpty(message = "name is required")
    @Size(message = "name must be equal to or lower than 100", min = 1, max = 100)
    private String name;
}
