package dev.tt.poc.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.tt.poc.orderservice.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
