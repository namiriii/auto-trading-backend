package com.namil.autotrading.dto;

import java.util.List;

public class OrderPageResponse {

    private List<OrderResponse> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public OrderPageResponse(List<OrderResponse> data,
                             int page,
                             int size,
                             long totalElements,
                             int totalPages) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<OrderResponse> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
