package com.lawProject.SSL.global.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;

    public PageInfo(int page, int size, int totalElements, int totalPages) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}