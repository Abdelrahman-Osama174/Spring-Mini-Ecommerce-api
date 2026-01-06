package com.abdelrahman.EcommerceApp.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {}
