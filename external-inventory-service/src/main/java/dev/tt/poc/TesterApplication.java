package dev.tt.poc;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

import dev.tt.poc.inventory.command.DomainEvent;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@SpringBootApplication
public class TesterApplication {

	@Autowired
	private RabbitMQProducer rabbitMQProducer;
	
	public static void main(String[] args) {
		SpringApplication.run(TesterApplication.class, args);
	}

	@Bean
    public CommandLineRunner conditionalRunner() {
        return args -> {
			InventoryRequest inventoryRequest = InventoryRequest.builder().id(UUID.randomUUID().toString())
					.status("Test status").build();
			
			DomainEvent<InventoryRequest> event = new DomainEvent<>();

	        event.setData(inventoryRequest);
	        event.setEvent("INVENTORY_REQUEST");
        	rabbitMQProducer.sendMessage(event);
            
        };
    }
}
