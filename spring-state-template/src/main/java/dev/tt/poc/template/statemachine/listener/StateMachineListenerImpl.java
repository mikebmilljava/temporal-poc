package dev.tt.poc.template.statemachine.listener;

import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class StateMachineListenerImpl extends StateMachineListenerAdapter<States, Events> {

    @Override
    public void stateChanged(State<States, Events> from, State<States, Events> to) {
        System.out.printf("→ State changed from %s to %s%n",
                from == null ? "NONE" : from.getId(), to.getId());
    }

    @Override
    public void eventNotAccepted(Message<Events> event) {
        System.err.println("⚠️  Event not accepted: " + event.getPayload());
    }

    @Override
    public void stateMachineError(org.springframework.statemachine.StateMachine<States, Events> sm, Exception exception) {
        System.err.println("❌ State machine error: " + exception.getMessage());
    }
}
