package dev.tt.poc.template.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Value
@Builder
@Jacksonized
public final class InternalData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    private String dataKey;
    private String dataValue;
    private Instant receivedAt;
    private Map<String, Object> metadata;

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

}