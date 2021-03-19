package com.example.springbootdocker.utils;

import com.example.springbootdocker.response.PageResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class PageResponseUtils {

    public <T> PageResponse<T> getPageResponse(String direction, String sortBy, Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .size(page.getSize())
                .page(page.getNumber())
                .sortBy(sortBy)
                .direction(direction)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}

