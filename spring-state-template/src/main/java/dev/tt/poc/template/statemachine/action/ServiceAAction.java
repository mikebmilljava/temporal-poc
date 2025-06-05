package dev.tt.poc.template.statemachine.action;

import dev.tt.poc.template.statemachine.domain.ProcessContext;
import dev.tt.poc.template.statemachine.domain.ThirdPartyData;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.service.ProcessActivities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class ServiceAAction implements Action<States, Events> {

    private final ProcessActivities activities;

    @Autowired
    public ServiceAAction(ProcessActivities activities) {
        this.activities = activities;
    }

    @Override
    public void execute(StateContext<States, Events> context) {
        // 1) Retrieve our ProcessContext from the extended state
        ProcessContext ctx = (ProcessContext) context.getExtendedState()
                .getVariables()
                .get("processContext");

        // 2) Call out to the Internal Data provider
        String requestId = ctx.getRequestId();
        ThirdPartyData data = activities.fetchThirdParty(requestId);

        // 3) Store the fetched data back into the ProcessContext
        ctx.setThirdPartyData(data);
        context.getExtendedState().getVariables().put("processContext", ctx);

        // 4) Fire the state‐machine event to advance the workflow
        context.getStateMachine().sendEvent(Events.SERVICE_A_REPLY);
    }
}

