package dev.tt.poc.template.statemachine.guard;

import dev.tt.poc.template.statemachine.domain.ProcessContext;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.machine.Events;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class DataReceivedGuard implements Guard<States, Events> {

    @Override
    public boolean evaluate(StateContext<States, Events> context) {
        // retrieve the process context from extended state variables
        ProcessContext ctx = context.getExtendedState()
                .get("processContext", ProcessContext.class);
        if (ctx == null) {
            // no context yet → do not accept internal signals
            return false;
        }
        // allow INTERNAL_SIGNAL only while received < expected
        return ctx.getReceivedCount() < ctx.getExpectedSignalCount();
    }
}

