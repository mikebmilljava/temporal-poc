package dev.tt.poc.template.statemachine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.machine.Events;

import static dev.tt.poc.template.statemachine.machine.States.*;

@Configuration
@EnableStateMachineFactory
public abstract class AbstractStateMachineConfig
        extends StateMachineConfigurerAdapter<States, Events> {

    @Override
    public final void configure(StateMachineStateConfigurer<States,Events> states) throws Exception {
        states.withStates()
                .initial(START)
                .state(SERVICE_A_WAIT, serviceAAction(), null)
                .state(SERVICE_B_WAIT, serviceBAction(), null)
                .state(SERVICE_C_WAIT, serviceCAction(), null)
                .state(SERVICE_D_WAIT, serviceDAction(), null)
                .state(SERVICE_E_WAIT, serviceEAction(), null)
                .end(COMPLETE);
    }

    @Override
    public final void configure(StateMachineTransitionConfigurer<States,Events> trans) throws Exception {
        configureTransitions(trans);
    }

    /** Subclasses must define their transitions here **/
    protected abstract void configureTransitions(StateMachineTransitionConfigurer<States,Events> trans) throws Exception;

    // hook methods for actions…
    protected abstract Action<States, Events> serviceAAction();
    protected abstract Action<States, Events> serviceBAction();
    protected abstract Action<States, Events> serviceCAction();
    protected abstract Action<States, Events> serviceDAction();
    protected abstract Action<States, Events> serviceEAction();

}
