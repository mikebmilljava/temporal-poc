package dev.tt.poc;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {

	@Bean
	MessageConverter messageConverter()
	{
	    return new Jackson2JsonMessageConverter();
	}
	
	
    @Bean public Queue queue()
    {
    	return new Queue("inventory-queue", true, false, false, Map.of(
                "x-queue-type", "quorum"
        ));
       
    }

    @Bean public Exchange exchange()
    {
        return new DirectExchange("inventory-exchange");
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange)
    {
        return BindingBuilder.bind(queue)
            .to(exchange)
            .with("inventory.general")
            .noargs();
    }
}