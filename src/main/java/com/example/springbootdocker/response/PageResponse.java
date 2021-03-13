package com.example.springbootdocker.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private int page;
    private int size;
    private String sortBy;
    private String direction;
    private int totalPages;
    private long totalElements;
    private List<T> content;

}