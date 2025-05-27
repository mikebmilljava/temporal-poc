package dev.tt.poc.subscription.activities;

import com.example.temporaltemplatepoc.domain.InternalData;
import com.example.temporaltemplatepoc.domain.ThirdPartyData;
import io.temporal.activity.ActivityOptions;
import io.temporal.activity.Activity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Component
public class ProcessActivitiesImpl implements ProcessAActivities, ProcessBActivities {

    private final WebClient webClient;
    private final RabbitTemplate rabbitTemplate;

    // Destination exchange/routing for internal requests:
    private static final String INTERNAL_EXCHANGE = "internal-exchange";
    private static final String INTERNAL_ROUTING_KEY = "internal.request";

    public ProcessActivitiesImpl(WebClient.Builder webClientBuilder,
                                 RabbitTemplate rabbitTemplate) {
        this.webClient = webClientBuilder
                .baseUrl("https://3rd-party.api")   // your third‐party base URL
                .build();
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ThirdPartyData fetchThirdParty(String requestId) {
        // Example: synchronous WebClient call with timeout
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/{req}")
                        .build(requestId))
                .retrieve()
                .bodyToMono(ThirdPartyData.class)
                .timeout(Duration.ofSeconds(30))
                .block();  // in Temporal activities, blocking is OK
    }

    @Override
    public void publishInternalRequests(String requestId) {
        // For illustration, publish N placeholder messages.
        int count = 3; // or make this configurable per process
        for (int i = 0; i < count; i++) {
            rabbitTemplate.convertAndSend(
                    INTERNAL_EXCHANGE,
                    INTERNAL_ROUTING_KEY,
                    requestId
            );
        }
    }

    @Override
    public void completeBusinessLogic(String requestId,
                                      ThirdPartyData tpData,
                                      List<InternalData> internalDatas) {
        // TODO: implement your real business steps here
        // For POC, maybe just log or store the aggregated result
        System.out.printf(
                "Request %s: completing business logic with TP=%s and %d internal items%n",
                requestId, tpData, internalDatas.size()
        );
    }
}