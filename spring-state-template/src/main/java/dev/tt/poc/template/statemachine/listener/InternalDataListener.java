package dev.tt.poc.template.statemachine.listener;

import dev.tt.poc.template.statemachine.domain.InternalData;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Component;

@Component
public class InternalDataListener {

    private static final String PROCESS_CTX_VAR = "processContext";

    private final StateMachineService<States, Events> smService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public InternalDataListener(StateMachineService<States, Events> smService,
                                  RabbitTemplate rabbitTemplate) {
        this.smService = smService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "internal-queue")
    public void onMessage(InternalData data) {
        String machineId = data.getRequestId();
        StateMachine<States, Events> sm = smService.acquireStateMachine(machineId);

        // If already timed‐out or complete, forward to DLQ
        States state = sm.getState().getId();
        if (state == States.TIMEOUT || state == States.COMPLETE) {
            rabbitTemplate.convertAndSend("dlx.exchange", "internal.dlq", data);
            smService.releaseStateMachine(machineId);
            return;
        }

        // existing logic to record data and send INTERNAL_SIGNAL…
        // e.g., ctx.incrementSignalCount(), ctx.addInternalData(data), sm.sendEvent(...)

        smService.releaseStateMachine(machineId);
    }
}

