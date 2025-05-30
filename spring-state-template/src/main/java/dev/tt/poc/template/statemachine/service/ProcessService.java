package dev.tt.poc.template.statemachine.service;

import dev.tt.poc.template.statemachine.domain.ProcessContext;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.machine.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessService {

    private final StateMachineFactory<States, Events> factory;

    @Autowired
    public ProcessService(StateMachineFactory<States, Events> factory) {
        this.factory = factory;
    }

    public StateMachine<States, Events> startProcess(
            String requestId,
            int expectedSignalCount
    ) {
        StateMachine<States, Events> sm = factory.getStateMachine(requestId);
        // Initialize shared context
        ProcessContext ctx = ProcessContext.builder()
                .requestId(requestId)
                .expectedSignalCount(expectedSignalCount)
                .build();
        sm.getExtendedState().getVariables().put("processContext", ctx);

        // Start the state machine and trigger the first transition
        sm.start();
        sm.sendEvent(Events.THIRD_PARTY_FETCHED);
        return sm;
    }
}