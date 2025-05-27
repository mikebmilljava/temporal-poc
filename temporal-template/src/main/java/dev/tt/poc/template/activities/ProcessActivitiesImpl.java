package dev.tt.poc.template.activities;

import dev.tt.poc.template.domain.InternalData;
import dev.tt.poc.template.domain.ThirdPartyData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
public class ProcessActivitiesImpl implements ProcessAActivities, ProcessBActivities {

    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    // Destination exchange/routing for internal requests
    private static final String INTERNAL_EXCHANGE    = "internal-exchange";
    private static final String INTERNAL_ROUTING_KEY = "internal.request";

    // Third-party URL template
    private static final String THIRD_PARTY_URL = "https://3rd-party.api/data/{req}";

    public ProcessActivitiesImpl(RestTemplate restTemplate,
                                 RabbitTemplate rabbitTemplate) {
        this.restTemplate   = Objects.requireNonNull(restTemplate, "restTemplate");
        this.rabbitTemplate = Objects.requireNonNull(rabbitTemplate, "rabbitTemplate");
    }

    @Override
    public ThirdPartyData fetchThirdParty(String requestId) {
        // Synchronous HTTP GET; will throw an exception on non-2xx or timeout
        return restTemplate.getForObject(THIRD_PARTY_URL, ThirdPartyData.class, requestId);
    }

    @Override
    public void publishInternalRequests(String requestId) {
        // For illustration, publish N placeholder messages
        int count = 3; // could be move to configuration or subclass-driven
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
        // TODO: real POC logic here
        System.out.printf(
                "Request %s: completing business logic with TP=%s and %d internal items%n",
                requestId, tpData, internalDatas.size()
        );
    }

    // --- Helper factory for RestTemplate with timeouts ---
    @Component("processRestTemplate")
    public static class RestTemplateFactory {

        @Bean
        public RestTemplate restTemplate() {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(10_000);  // 10s connect
            factory.setReadTimeout(30_000);     // 30s read
            return new RestTemplate(factory);
        }
    }
}
