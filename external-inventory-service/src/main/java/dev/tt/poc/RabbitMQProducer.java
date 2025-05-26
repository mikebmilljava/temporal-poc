package dev.tt.poc;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.tt.poc.inventory.command.DomainEvent;

@Component
public class RabbitMQProducer {

    @Autowired private RabbitTemplate rabbitTemplate;

    public void sendMessage(DomainEvent<?> event )
    {
        rabbitTemplate.convertAndSend(
            "inventory-exchange", "inventory.general", event);
    }
}