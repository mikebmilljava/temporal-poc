package dev.tt.poc.template.statemachine.action;

import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;


import java.util.List;


public class CompositeAction implements Action<States, Events> {

    private final List<Action<States, Events>> actions;

    public CompositeAction(List<Action<States, Events>> actions) {
        this.actions = actions;
    }

    @Override
    public void execute(StateContext<States, Events> context) {
        actions.forEach(a -> a.execute(context));
    }
}
