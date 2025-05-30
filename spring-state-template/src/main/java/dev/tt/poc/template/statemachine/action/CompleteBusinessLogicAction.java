package dev.tt.poc.template.statemachine.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import dev.tt.poc.template.statemachine.service.ProcessActivities;
import dev.tt.poc.template.statemachine.domain.ProcessContext;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;

@Component
public class CompleteBusinessLogicAction implements Action<States, Events> {

    private final ProcessActivities activities;

    @Autowired
    public CompleteBusinessLogicAction(ProcessActivities activities) {
        this.activities = activities;
    }

    @Override
    public void execute(StateContext<States, Events> context) {
        // Retrieve the shared ProcessContext from extended state
        ProcessContext ctx = context.getExtendedState()
                .get("processContext", ProcessContext.class);

        // Execute business logic with fetched and internal data
        activities.completeBusinessLogic(
                ctx.getRequestId(),
                ctx.getThirdPartyData(),
                ctx.getReceivedData()
        );

        // Signal completion of business logic
        context.getStateMachine().sendEvent(Events.BUSINESS_COMPLETE);
    }
}

