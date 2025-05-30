package dev.tt.poc.template.statemachine.action;

import dev.tt.poc.template.statemachine.domain.ProcessContext;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class WaitForDataAction implements Action<States, Events> {

    private static final String PROCESS_CTX_VAR = "processContext";

    @Override
    public void execute(StateContext<States, Events> context) {
        // Retrieve the shared ProcessContext from the extended state
        ProcessContext ctx = (ProcessContext) context.getExtendedState()
                .getVariables()
                .get(PROCESS_CTX_VAR);

        if (ctx != null) {
            int received = ctx.getReceivedData().size();
            int expected = ctx.getExpectedSignalCount();

            if (received >= expected) {
                // All signals received—send the complete event
                context.getStateMachine().sendEvent(Events.INTERNAL_COMPLETE);
            }
        }
    }
}

