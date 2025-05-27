package dev.tt.poc.subscription.dto;

import dev.tt.poc.subscription.domain.InternalData;

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
        return new InternalData(dataKey, dataValue, requestId);
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
