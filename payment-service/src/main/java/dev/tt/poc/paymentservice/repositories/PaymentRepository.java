package dev.tt.poc.paymentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.tt.poc.paymentservice.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
