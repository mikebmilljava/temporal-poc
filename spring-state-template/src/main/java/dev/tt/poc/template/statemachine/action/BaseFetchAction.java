package dev.tt.poc.template.statemachine.action;

import dev.tt.poc.template.statemachine.client.ThirdPartyClient;
import dev.tt.poc.template.statemachine.domain.ThirdPartyData;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BaseFetchAction implements Action<States, Events> {

    private final Map<String,ThirdPartyClient> clients;

    @Autowired
    public BaseFetchAction(List<ThirdPartyClient> clients) {
        // turn the list into a lookup map:
        this.clients = clients.stream()
                .collect(Collectors.toMap(ThirdPartyClient::getName, Function.identity()));
    }

    @Override
    public void execute(StateContext<States,Events> ctx) {
        String requestId    = ctx.getExtendedState().get("requestId", String.class);
        String providerName = ctx.getExtendedState().get("provider",  String.class);
        ThirdPartyClient client = clients.get(providerName);
        if (client == null) {
            throw new IllegalStateException("No such provider: "+ providerName);
        }

        ThirdPartyData data = client.fetch(requestId);
        ctx.getExtendedState().getVariables().put("thirdPartyData", data);
        ctx.getStateMachine().sendEvent(Events.THIRD_PARTY_FETCHED);
    }
}
