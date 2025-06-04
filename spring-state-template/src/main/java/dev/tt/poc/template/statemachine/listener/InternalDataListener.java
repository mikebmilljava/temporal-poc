package dev.tt.poc.template.statemachine.listener;

import dev.tt.poc.template.statemachine.domain.InternalData;
import dev.tt.poc.template.statemachine.domain.ProcessContext;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static dev.tt.poc.template.statemachine.config.RabbitConfig.*;

@Component
@Slf4j
public class InternalDataListener {

    public static final String PROCESS_CTX_VAR = "processContext";

    private final StateMachineService<States, Events> smService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public InternalDataListener(StateMachineService<States, Events> smService,
                                  RabbitTemplate rabbitTemplate) {
        this.smService = smService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "raw-internal-queue")
    public void onRawInternalMessage(@Payload InternalData data) {
        String requestId = data.getRequestId();

        log.info("Message received with {}",requestId);
        // (Optional) Enrich or validate the incoming data here, e.g. save to a DB.
        // Now publish an INTERNAL_SIGNAL event into RabbitMQ:
        rabbitTemplate.convertAndSend(
                INTERNAL_SIGNAL_DIRECT_EXCHANGE,       // from RabbitConfig
                INTERNAL_SIGNAL_ROUTING_KEY,    // from RabbitConfig
                data);
    }

    @RabbitListener(queues = "internal-queue")
    public void onInternalSignal(@Payload InternalData data) {
        log.info("Listener got InternalData for requestId={}",data.getRequestId());
        String machineId = data.getRequestId();
        StateMachine<States, Events> sm = smService.acquireStateMachine(machineId);

        try {
            ProcessContext ctx = sm.getExtendedState()
                    .get(PROCESS_CTX_VAR, ProcessContext.class);

            if (ctx == null) {
                smService.releaseStateMachine(machineId);
                return;
            }

            States currentState = sm.getState().getId();
            if (currentState == States.TIMEOUT || currentState == States.COMPLETE) {
                rabbitTemplate.convertAndSend(
                        DEAD_LETTER_QUEUE_DIRECT_EXCHANGE,          // your dead-letter exchange
                        DEAD_LETTER_ROUTING_KEY,          // your dead-letter routing key
                        data
                );
                return;
            }

            // Record the new InternalData
            ctx.addInternalData(data);

            // Fire the INTERNAL_SIGNAL event
            sm.sendEvent(Mono.just(MessageBuilder.withPayload(Events.INTERNAL_SIGNAL).build()));

        } finally {
            smService.releaseStateMachine(machineId);
        }
    }

}

