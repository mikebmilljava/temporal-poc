package dev.tt.poc.orderservice.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.tt.poc.orderservice.models.Order;
import dev.tt.poc.orderservice.models.OrderLine;
import dev.tt.poc.orderservice.repositories.OrderRepository;
import dev.tt.poc.workflow.activities.OrderActivity;

import java.util.Map;

@Component
@Slf4j
public class OrderActivityImpl implements OrderActivity {
    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Long placeOrder(String customerId, Map<Long, Integer> productLines) {
//
        var order = new Order();
        order.setCustomerId(customerId);
        order.setStatus("PLACED");
        order.setLines(productLines.entrySet().stream()
                .map(entry -> {
                    var line = new OrderLine();
                    line.setProductId(entry.getKey());
                    line.setQuantity(entry.getValue());
                    return line;
                })
                .toList());

        var savedOrder = orderRepository.save(order);
        log.info("Order {} placed for customer {}", savedOrder.getId(), customerId);
        return savedOrder.getId();
    }

    @Override
    public void cancelOrder(Long orderId) {
        updateOrder(orderId, "CANCELLED");
    }


    @Override
    public void completeOrder(Long orderId) {
        updateOrder(orderId, "COMPLETED");
    }

    private void updateOrder(Long orderId, String status) {
        var oder = orderRepository.findById(orderId);
        if (oder.isPresent()) {
            var order = oder.get();
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}
