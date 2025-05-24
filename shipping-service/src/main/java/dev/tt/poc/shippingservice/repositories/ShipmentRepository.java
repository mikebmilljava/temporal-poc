package dev.tt.poc.shippingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.tt.poc.shippingservice.models.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
