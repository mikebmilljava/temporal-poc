package com.datmt.temporal.orderservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String customerId;

    @ElementCollection
    private List<OrderLine> lines;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
