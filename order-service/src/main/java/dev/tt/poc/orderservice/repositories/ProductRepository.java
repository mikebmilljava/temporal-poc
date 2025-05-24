package dev.tt.poc.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tt.poc.orderservice.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
