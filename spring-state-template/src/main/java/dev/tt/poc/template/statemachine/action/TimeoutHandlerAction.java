package dev.tt.poc.template.statemachine.action;

import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Component
public class TimeoutHandlerAction implements Action<States, Events> {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TimeoutHandlerAction(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void execute(StateContext<States, Events> context) {
        // When TIMEOUT state is entered, forward to DLQ
        String requestId = context.getExtendedState()
                .get("requestId", String.class);
        rabbitTemplate.convertAndSend("dlx.exchange", "internal.dlq", requestId);
    }
}
