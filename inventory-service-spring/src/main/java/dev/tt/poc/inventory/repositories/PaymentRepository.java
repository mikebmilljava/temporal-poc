package dev.tt.poc.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.tt.poc.inventory.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
