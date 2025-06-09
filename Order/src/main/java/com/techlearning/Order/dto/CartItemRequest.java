package com.techlearning.Order.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private String productId;
    private Integer stockQuantity;
}
