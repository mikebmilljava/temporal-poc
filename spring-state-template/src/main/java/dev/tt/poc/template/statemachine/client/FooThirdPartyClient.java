package dev.tt.poc.template.statemachine.client;

import dev.tt.poc.template.statemachine.domain.ThirdPartyData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FooThirdPartyClient implements ThirdPartyClient {

    public final static String CLIENT_NAME = "foo";

    private final RestTemplate rest;
    private final String url;

    public FooThirdPartyClient(RestTemplate rest,@Value("${thirdparty.foo.url:UNKNOWN}") String url) {
        this.rest = rest;
        this.url = url;
    }

    @Override
    public String getName() { return CLIENT_NAME; }

    @Override
    public ThirdPartyData fetch(String requestId) {
        return rest.getForObject(url, ThirdPartyData.class, requestId);
    }
}
