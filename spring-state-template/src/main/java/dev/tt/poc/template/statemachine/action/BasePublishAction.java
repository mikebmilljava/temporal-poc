package dev.tt.poc.template.statemachine.action;

import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class BasePublishAction implements Action<States, Events> {

    private final RabbitTemplate rabbit;

    @Autowired
    public BasePublishAction(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @Override
    public void execute(StateContext<States, Events> ctx) {
        // 1) pull the requestId header
        String requestId = ctx.getMessage()
                .getHeaders()
                .get("requestId", String.class);

        // 2) pull expected count out of extended state
        Integer expectedCount = ctx.getExtendedState()
                .get("expectedCount", Integer.class);
        if (expectedCount == null) {
            expectedCount = 0;
        }

        // 3) fire off that many “internal” messages
        for (int i = 0; i < expectedCount; i++) {
            rabbit.convertAndSend(
                    "internal-exchange",   // your exchange
                    "internal-routing-key",// or whatever routing key you need
                    requestId
            );
        }

        // (no state machine event here – it will resume on the INTERNAL_SIGNAL events
        // coming back in via your listener)
    }
}
