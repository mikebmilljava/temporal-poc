package dev.tt.poc.template.statemachine.config;

import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static dev.tt.poc.template.statemachine.machine.States.*;

@Configuration
@EnableStateMachineFactory
public abstract class AbstractStateMachineConfig
        extends StateMachineConfigurerAdapter<States, Events> {

    @Override
    public final void configure(StateMachineStateConfigurer<States,Events> states) throws Exception {
        states.withStates()
                .initial(START)
                .state(FETCH_THIRD_PARTY, fetchAction(), null)
                .state(PUBLISH_INTERNAL, publishAction(), null)
                .state(WAIT_FOR_INTERNAL, waitAction(), null)
                .state(TIMEOUT, timeoutHandler(), null)
                .state(COMPLETE, completeAction(), null)
                .end(END);
    }

    @Override
    public final void configure(StateMachineTransitionConfigurer<States,Events> trans) throws Exception {
        configureTransitions(trans);
    }

    /** Subclasses must define their transitions here **/
    protected abstract void configureTransitions(StateMachineTransitionConfigurer<States,Events> trans) throws Exception;

    // hook methods for actions…
    protected abstract Action<States, Events> fetchAction();
    protected abstract Action<States, Events> publishAction();
    protected abstract Action<States, Events> waitAction();
    protected abstract Action<States, Events> timeoutHandler();
    protected abstract Action<States, Events> completeAction();
}
