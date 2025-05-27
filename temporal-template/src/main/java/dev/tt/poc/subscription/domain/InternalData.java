package dev.tt.poc.subscription.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class InternalData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String requestId;
    private final String dataKey;
    private final String dataValue;
    private final Instant receivedAt;
    private final Map<String, Object> metadata;

    private InternalData(Builder b) {
        this.requestId  = Objects.requireNonNull(b.requestId, "requestId");
        this.dataKey    = Objects.requireNonNull(b.dataKey, "dataKey");
        this.dataValue  = Objects.requireNonNull(b.dataValue, "dataValue");
        this.receivedAt = Objects.requireNonNull(b.receivedAt, "receivedAt");
        this.metadata   = b.metadata == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(b.metadata);
    }

    public InternalData(String dataKey, String dataValue, String requestId) {
        this.dataKey   = dataKey;
        this.dataValue = dataValue;
        this.requestId = requestId;
        this.receivedAt = Instant.now();
        this.metadata   = Collections.emptyMap();
    }

    public String getRequestId()   { return requestId; }
    public String getDataKey()     { return dataKey; }
    public String getDataValue()   { return dataValue; }
    public Instant getReceivedAt() { return receivedAt; }
    public Map<String,Object> getMetadata() { return metadata; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InternalData)) return false;
        InternalData that = (InternalData) o;
        return requestId.equals(that.requestId)
                && dataKey.equals(that.dataKey)
                && dataValue.equals(that.dataValue)
                && receivedAt.equals(that.receivedAt)
                && metadata.equals(that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, dataKey, dataValue, receivedAt, metadata);
    }

    @Override
    public String toString() {
        return "InternalData{" +
                "requestId='" + requestId + '\'' +
                ", dataKey='" + dataKey + '\'' +
                ", dataValue='" + dataValue + '\'' +
                ", receivedAt=" + receivedAt +
                ", metadata=" + metadata +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String requestId;
        private String dataKey;
        private String dataValue;
        private Instant receivedAt;
        private Map<String, Object> metadata;

        private Builder() {}

        public Builder requestId(String requestId) {
            this.requestId = requestId; return this;
        }
        public Builder dataKey(String dataKey) {
            this.dataKey = dataKey; return this;
        }
        public Builder dataValue(String dataValue) {
            this.dataValue = dataValue; return this;
        }
        public Builder receivedAt(Instant receivedAt) {
            this.receivedAt = receivedAt; return this;
        }
        public Builder metadata(Map<String,Object> metadata) {
            this.metadata = metadata; return this;
        }
        public InternalData build() {
            return new InternalData(this);
        }
    }
}