package dev.tt.poc.template.statemachine.config;

import dev.tt.poc.template.statemachine.action.*;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import static dev.tt.poc.template.statemachine.machine.Events.*;
import static dev.tt.poc.template.statemachine.machine.States.*;

// ProcessAStateMachineConfig.java
@Configuration
public class ProcessAStateMachineConfig extends AbstractStateMachineConfig {

    private final FetchThirdPartyAction fetchAction;
    private final BasePublishAction publishAction;
    private final WaitForDataAction waitAction;
    private final TimeoutHandlerAction timeoutHandler;
    private final CompleteBusinessLogicAction completeAction;

    @Autowired
    public ProcessAStateMachineConfig(
            FetchThirdPartyAction fetchAction,
            BasePublishAction publishAction,
            WaitForDataAction waitAction,
            TimeoutHandlerAction timeoutHandler,
            CompleteBusinessLogicAction completeAction) {
        this.fetchAction = fetchAction;
        this.publishAction = publishAction;
        this.waitAction = waitAction;
        this.timeoutHandler = timeoutHandler;
        this.completeAction = completeAction;
    }

    @Override
    protected void configureTransitions(StateMachineTransitionConfigurer<States, Events> trans) throws Exception {
        trans
                .withExternal().source(START).target(FETCH_THIRD_PARTY)
                .event(THIRD_PARTY_FETCHED)
                .and().withExternal().source(FETCH_THIRD_PARTY).target(PUBLISH_INTERNAL)
                .event(THIRD_PARTY_FETCHED)
                .and().withExternal().source(PUBLISH_INTERNAL).target(WAIT_FOR_INTERNAL)
                .event(INTERNAL_SIGNAL)
                .and().withExternal().source(WAIT_FOR_INTERNAL).target(TIMEOUT)
                .event(INTERNAL_TIMEOUT)
                .and().withExternal().source(WAIT_FOR_INTERNAL).target(COMPLETE)
                .event(INTERNAL_COMPLETE)
                .and().withExternal().source(COMPLETE).target(END)
                .event(BUSINESS_COMPLETE);
    }

    @Override public Action<States, Events> fetchAction()    { return fetchAction; }
    @Override public Action<States, Events> publishAction()  { return publishAction; }
    @Override public Action<States, Events> waitAction()     { return waitAction; }
    @Override public Action<States, Events> timeoutHandler() { return timeoutHandler; }
    @Override public Action<States, Events> completeAction() { return completeAction; }

}
