package dev.tt.poc.template.statemachine.config;

import dev.tt.poc.template.statemachine.action.*;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.machine.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;

@Configuration
public class BaseStateMachineConfig extends StateMachineConfigurerAdapter<States, Events> {

    private final FetchThirdPartyAction fetchAction;
    private final BasePublishAction publishAction;
    private final WaitForDataAction waitAction;
    private final CompleteBusinessLogicAction completeAction;
    private final TimeoutSchedulerAction timeoutScheduler;
    private final TimeoutHandlerAction timeoutHandler;

    @Autowired
    public BaseStateMachineConfig(
    FetchThirdPartyAction fetchAction,
    BasePublishAction publishAction,
    WaitForDataAction waitAction,
    CompleteBusinessLogicAction completeAction,
    TimeoutSchedulerAction timeoutScheduler,
    TimeoutHandlerAction timeoutHandler)
    {
        this.fetchAction = fetchAction;
        this.publishAction = publishAction;
        this.waitAction = waitAction;
        this.completeAction = completeAction;
        this.timeoutScheduler = timeoutScheduler;
        this.timeoutHandler = timeoutHandler;
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.START)
                .state(States.FETCH_THIRD_PARTY, fetchAction, null)
                .state(States.PUBLISH_INTERNAL, publishAction, null)
                .state(States.WAIT_FOR_INTERNAL,
                        new CompositeAction(Arrays.asList(waitAction, timeoutScheduler)),
                        null)
                .state(States.TIMEOUT, timeoutHandler, null)
                .state(States.COMPLETE, completeAction, null)
                .end(States.END);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal().source(States.START).target(States.FETCH_THIRD_PARTY)
                .event(Events.THIRD_PARTY_FETCHED)
                .and().withExternal().source(States.FETCH_THIRD_PARTY).target(States.PUBLISH_INTERNAL)
                .event(Events.THIRD_PARTY_FETCHED)
                .and().withExternal().source(States.PUBLISH_INTERNAL).target(States.WAIT_FOR_INTERNAL)
                .event(Events.INTERNAL_SIGNAL)
                .and().withExternal().source(States.WAIT_FOR_INTERNAL).target(States.TIMEOUT)
                .event(Events.INTERNAL_TIMEOUT)
                .and().withExternal().source(States.WAIT_FOR_INTERNAL).target(States.COMPLETE)
                .event(Events.INTERNAL_COMPLETE)
                .and().withExternal().source(States.COMPLETE).target(States.END)
                .event(Events.BUSINESS_COMPLETE);
    }
}

