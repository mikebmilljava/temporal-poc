package dev.tt.poc.template.statemachine.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String INTERNAL_SIGNAL_ROUTING_KEY = "internal.signal.routing-key";
    public static final String INTERNAL_SIGNAL_DIRECT_EXCHANGE = "internal-signal-exchange";
    public static final String DEAD_LETTER_QUEUE_DIRECT_EXCHANGE = "dlx.exchange";
    public static final String DEAD_LETTER_ROUTING_KEY = "internal.dlq";

    @Bean
    public Queue internalQueue(@Value("${process.signal.ttl.ms:30000}") int ttl) {
        return QueueBuilder.durable("internal-queue")
                .withArguments(Map.of(
                        "x-dead-letter-exchange", DEAD_LETTER_QUEUE_DIRECT_EXCHANGE,
                        "x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY,
                        "x-message-ttl", ttl
                ))
                .build();
    }

    @Bean
    public DirectExchange internalSignalExchange() {
        return new DirectExchange(INTERNAL_SIGNAL_DIRECT_EXCHANGE);
    }

    @Bean
    public Binding internalSignalBinding(Queue internalQueue, DirectExchange internalSignalExchange) {
        return BindingBuilder
                .bind(internalQueue)                      // queue: "internal-queue"
                .to(internalSignalExchange)
                .with(INTERNAL_SIGNAL_ROUTING_KEY);     // pick any string
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DEAD_LETTER_QUEUE_DIRECT_EXCHANGE);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_ROUTING_KEY).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(dlxExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }
}



