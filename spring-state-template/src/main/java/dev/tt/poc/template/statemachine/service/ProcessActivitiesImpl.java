package dev.tt.poc.template.statemachine.service;

import dev.tt.poc.template.statemachine.client.ThirdPartyClient;
import dev.tt.poc.template.statemachine.domain.InternalData;
import dev.tt.poc.template.statemachine.domain.ThirdPartyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProcessActivitiesImpl implements ProcessActivities {
    private final Map<String, ThirdPartyClient> clients;
    private final String defaultClient;

    @Autowired
    public ProcessActivitiesImpl(
            List<ThirdPartyClient> clientBeans,
            @Value("${thirdparty.default:foo}") String defaultClient
    ) {
        this.clients = clientBeans.stream()
                .collect(Collectors.toMap(ThirdPartyClient::getName, Function.identity()));
        this.defaultClient = defaultClient;
    }

    @Override
    public ThirdPartyData fetchThirdParty(String requestId) {
        // pick a client (could also pass clientName in context)
        ThirdPartyClient client = clients.getOrDefault(defaultClient,
                clients.values().iterator().next());
        return client.fetch(requestId);
    }

    @Override
    public void completeBusinessLogic(
            String requestId,
            ThirdPartyData fetchedData,
            List<InternalData> internalDataList
    ) {
        // real logic…
    }
}
