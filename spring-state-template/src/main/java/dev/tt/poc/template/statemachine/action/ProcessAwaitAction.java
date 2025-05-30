package dev.tt.poc.template.statemachine.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.machine.Events;
import org.springframework.stereotype.Component;

@Component
public class ProcessAwaitAction implements Action<States, Events> {

    private final int expected;

    @Autowired
    public ProcessAwaitAction(@Value("${process.internal.expected:3}")int expected) {
        this.expected = expected;
    }

    @Override
    public void execute(StateContext<States, Events> ctx) {
        Integer count = ctx.getExtendedState()
                .get("count", Integer.class);
        if (count != null && count >= expected) {
            ctx.getStateMachine().sendEvent(Events.INTERNAL_COMPLETE);
        }
    }
}
