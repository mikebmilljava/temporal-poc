package dev.tt.poc.template.dto;

import dev.tt.poc.template.domain.InternalData;

import java.time.Instant;
import java.util.Collections;

public class InternalDataMessage {

    private String requestId;

    // payload fields - add as needed
    private String dataKey;
    private String dataValue;

    public InternalDataMessage() { }

    public InternalDataMessage(String requestId, String dataKey, String dataValue) {
        this.requestId = requestId;
        this.dataKey   = dataKey;
        this.dataValue = dataValue;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public InternalData toInternalData() {

        return InternalData.builder()
                .requestId(requestId)
                .dataKey(dataKey)
                .dataValue(dataValue)
                .receivedAt(Instant.now())
                .metadata(Collections.emptyMap())
                .build();
    }

    @Override
    public String toString() {
        return "InternalDataMessage{" +
                "requestId='" + requestId + '\'' +
                ", dataKey='"   + dataKey   + '\'' +
                ", dataValue='" + dataValue + '\'' +
                '}';
    }
}
