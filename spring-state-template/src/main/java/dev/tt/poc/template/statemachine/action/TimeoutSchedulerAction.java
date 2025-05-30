package dev.tt.poc.template.statemachine.action;

import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.scheduling.TaskScheduler;
import java.time.Instant;

@Component
public class TimeoutSchedulerAction implements Action<States, Events> {


    private final TaskScheduler scheduler;
    private final StateMachineService<States, Events> stateMachineService;
    private final long timeoutMs;

    @Autowired
    public TimeoutSchedulerAction(
        TaskScheduler scheduler,
        StateMachineService<States, Events> stateMachineService,
        @Value("${process.timeout.ms:30000}") long timeoutMs)
    {
        this.scheduler = scheduler;
        this.stateMachineService = stateMachineService;
        this.timeoutMs = timeoutMs;
    }

    @Override
    public void execute(StateContext<States, Events> context) {
        String machineId = context.getStateMachine().getId();
        // Schedule the timeout event
        scheduler.schedule(() -> {
            stateMachineService.acquireStateMachine(machineId)
                    .sendEvent(Events.INTERNAL_TIMEOUT);
            stateMachineService.releaseStateMachine(machineId);
        }, Instant.now().plusMillis(timeoutMs));
    }
}