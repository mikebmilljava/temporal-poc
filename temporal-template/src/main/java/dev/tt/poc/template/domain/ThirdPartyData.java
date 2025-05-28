package dev.tt.poc.template.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Value
@Builder
@Jacksonized
public class ThirdPartyData {
    String requestId;
    Map<String, Object> payload;
    Instant fetchedAt;
    String sourceSystem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThirdPartyData)) return false;
        ThirdPartyData that = (ThirdPartyData) o;
        return requestId.equals(that.requestId)
                && fetchedAt.equals(that.fetchedAt)
                && sourceSystem.equals(that.sourceSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, payload, fetchedAt, sourceSystem);
    }

    @Override
    public String toString() {
        return "ThirdPartyData{" +
                "requestId='" + requestId + '\'' +
                ", payload=" + payload +
                ", fetchedAt=" + fetchedAt +
                ", sourceSystem='" + sourceSystem + '\'' +
                '}';
    }
}