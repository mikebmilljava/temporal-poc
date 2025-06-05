package dev.tt.poc.template.statemachine.config;

import dev.tt.poc.template.statemachine.action.*;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
public class RegistrationMachineConfig extends AbstractStateMachineConfig {

    private final ServiceAAction serviceAAction;
    private final ServiceBAction serviceBAction;
    private final ServiceCAction serviceCAction;
    private final ServiceDAction serviceDAction;
    private final ServiceEAction serviceEAction;

    @Autowired
    public RegistrationMachineConfig(
            ServiceAAction serviceAAction,
            ServiceBAction serviceBAction,
            ServiceCAction serviceCAction,
            ServiceDAction serviceDAction,
            ServiceEAction serviceEAction) {
        this.serviceAAction = serviceAAction;
        this.serviceBAction = serviceBAction;
        this.serviceCAction = serviceCAction;
        this.serviceDAction = serviceDAction;
        this.serviceEAction = serviceEAction;
    }

    protected void configureTransitions(StateMachineTransitionConfigurer<States,Events> trans) throws Exception {
        trans
                .withExternal()
                .source(States.START).target(States.SERVICE_A_WAIT)
                .event(Events.BEGIN_PROCESS)
                .and()
                .withExternal()
                .source(States.SERVICE_A_WAIT).target(States.SERVICE_B_WAIT)
                .event(Events.SERVICE_A_REPLY)
                .and()
                .withExternal()
                .source(States.SERVICE_B_WAIT).target(States.SERVICE_C_WAIT)
                .event(Events.SERVICE_B_REPLY)
                .and()
                .withExternal()
                .source(States.SERVICE_C_WAIT).target(States.SERVICE_D_WAIT)
                .event(Events.SERVICE_C_REPLY)
                .and()
                .withExternal()
                .source(States.SERVICE_D_WAIT).target(States.SERVICE_E_WAIT)
                .event(Events.SERVICE_D_REPLY)
                .and()
                .withExternal()
                .source(States.SERVICE_E_WAIT).target(States.COMPLETE)
                .event(Events.SERVICE_E_REPLY);
    }


    @Override public Action<States, Events> serviceAAction() { return serviceAAction; }
    @Override public Action<States, Events> serviceBAction() { return serviceBAction; }
    @Override public Action<States, Events> serviceCAction() { return serviceCAction; }
    @Override public Action<States, Events> serviceDAction() { return serviceDAction; }
    @Override public Action<States, Events> serviceEAction() { return serviceEAction; }
}
