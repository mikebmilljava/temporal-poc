package dev.tt.poc.template.statemachine.client;

import dev.tt.poc.template.statemachine.domain.ThirdPartyData;

public interface ThirdPartyClient {
    String getName();                        // e.g. “foo”, “bar”
    ThirdPartyData fetch(String requestId);
}