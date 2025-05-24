package dev.tt.poc.shippingservice.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.tt.poc.shippingservice.models.Shipment;
import dev.tt.poc.shippingservice.repositories.ShipmentRepository;
import dev.tt.poc.workflow.activities.ShippingActivity;

@Component
@Slf4j
public class ShippingActivityImpl implements ShippingActivity {
    private ShipmentRepository shipmentRepository;

    @Autowired
    public void setShipmentRepository(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void processShipment(Long orderId) {
        log.info("Shipment processed for order id: {}:", orderId);
        var shipment = new Shipment();
        shipment.setOrderId(orderId);
        shipment.setStatus("SHIPPED");
        shipmentRepository.save(shipment);

        log.info("Shipment saved successfully for order id: {}", orderId);
    }

}
