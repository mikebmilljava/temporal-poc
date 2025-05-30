package dev.tt.poc.template.statemachine.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue internalQueue(@Value("${process.signal.ttl.ms:30000}") int ttl) {
        return QueueBuilder.durable("internal-queue")
                .withArguments(Map.of(
                        "x-dead-letter-exchange", "dlx.exchange",
                        "x-dead-letter-routing-key", "internal.dlq",
                        "x-message-ttl", ttl
                ))
                .build();
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("dlx.exchange");
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("internal.dlq").build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(dlxExchange())
                .with("internal.dlq");
    }
}



