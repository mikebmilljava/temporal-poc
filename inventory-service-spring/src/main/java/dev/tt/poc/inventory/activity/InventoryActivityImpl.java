package dev.tt.poc.inventory.activity;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import dev.tt.poc.inventory.InventoryRequest;
import dev.tt.poc.inventory.command.DomainEvent;
import dev.tt.poc.inventory.repositories.PaymentRepository;
import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ActivityImpl(workers = "inventory-service-worker")
public class InventoryActivityImpl implements InventoryActivity {
	
	 
	private final RabbitTemplate rabbitTemplate;

    private final PaymentRepository paymentRepository;
    
    public InventoryActivityImpl(PaymentRepository paymentRepository, RabbitTemplate rabbitTemplate) {
		this.paymentRepository = paymentRepository;
		this.rabbitTemplate = rabbitTemplate;
		log.info("InventoryActivityImpl initialized with PaymentRepository and RabbitTemplate");
		
	}
    
	@Override
	public void processInventory(String workflowId, Long orderId, double amount) {
		
		DomainEvent<InventoryRequest> event = new DomainEvent<>();
		InventoryRequest inventoryRequest = InventoryRequest.builder().id(UUID.randomUUID().toString())
				.status("Test status").build();
		
        event.setCorrelationId(workflowId);
		event.setData(inventoryRequest);
        event.setEvent("INVENTORY_REQUEST");
        
        rabbitTemplate.convertAndSend(
                "inventory-exchange", "inventory.general", event);
        
        
		log.info("processInventoryRequest for workflowId: {}", workflowId);
	}
	
  
}
