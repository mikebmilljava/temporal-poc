package dev.tt.poc.orderservice.controllers;

import java.util.Map;

public record CreateOrderRequest(
        String customerId,
        Map<Long, Integer> items
) {
}
