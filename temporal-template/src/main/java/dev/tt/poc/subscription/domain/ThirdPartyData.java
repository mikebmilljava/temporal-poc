package dev.tt.poc.subscription.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class ThirdPartyData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String requestId;
    private final Map<String, Object> payload;
    private final Instant fetchedAt;
    private final String sourceSystem;

    private ThirdPartyData(Builder b) {
        this.requestId    = Objects.requireNonNull(b.requestId, "requestId");
        this.payload      = b.payload == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(b.payload);
        this.fetchedAt    = Objects.requireNonNull(b.fetchedAt, "fetchedAt");
        this.sourceSystem = Objects.requireNonNull(b.sourceSystem, "sourceSystem");
    }

    public String getRequestId()    { return requestId; }
    public Map<String,Object> getPayload()   { return payload; }
    public Instant getFetchedAt()   { return fetchedAt; }
    public String getSourceSystem() { return sourceSystem; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThirdPartyData)) return false;
        ThirdPartyData that = (ThirdPartyData) o;
        return requestId.equals(that.requestId)
                && fetchedAt.equals(that.fetchedAt)
                && sourceSystem.equals(that.sourceSystem)
                && payload.equals(that.payload);
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String requestId;
        private Map<String, Object> payload;
        private Instant fetchedAt;
        private String sourceSystem;

        private Builder() {}

        public Builder requestId(String requestId) {
            this.requestId = requestId; return this;
        }
        public Builder payload(Map<String,Object> payload) {
            this.payload = payload; return this;
        }
        public Builder fetchedAt(Instant fetchedAt) {
            this.fetchedAt = fetchedAt; return this;
        }
        public Builder sourceSystem(String sourceSystem) {
            this.sourceSystem = sourceSystem; return this;
        }
        public ThirdPartyData build() {
            return new ThirdPartyData(this);
        }
    }
}