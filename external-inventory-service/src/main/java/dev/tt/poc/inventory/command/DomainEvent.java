package dev.tt.poc.inventory.command;


import java.io.Serializable;
import java.time.LocalDateTime;


public class DomainEvent<T> implements Serializable {
    private String correlationId;
    private String event;
    private LocalDateTime timestamp;
    private String sourceService;
    private int version;
    private T data;

    public DomainEvent() {
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public String getEvent() {
        return this.event;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getSourceService() {
        return this.sourceService;
    }

    public int getVersion() {
        return this.version;
    }

    public T getData() {
        return this.data;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSourceService(String sourceService) {
        this.sourceService = sourceService;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DomainEvent)) return false;
        final DomainEvent<?> other = (DomainEvent<?>) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$correlationId = this.getCorrelationId();
        final Object other$correlationId = other.getCorrelationId();
        if (this$correlationId == null ? other$correlationId != null : !this$correlationId.equals(other$correlationId))
            return false;
        final Object this$event = this.getEvent();
        final Object other$event = other.getEvent();
        if (this$event == null ? other$event != null : !this$event.equals(other$event)) return false;
        final Object this$timestamp = this.getTimestamp();
        final Object other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !this$timestamp.equals(other$timestamp)) return false;
        final Object this$sourceService = this.getSourceService();
        final Object other$sourceService = other.getSourceService();
        if (this$sourceService == null ? other$sourceService != null : !this$sourceService.equals(other$sourceService))
            return false;
        if (this.getVersion() != other.getVersion()) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DomainEvent;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $correlationId = this.getCorrelationId();
        result = result * PRIME + ($correlationId == null ? 43 : $correlationId.hashCode());
        final Object $event = this.getEvent();
        result = result * PRIME + ($event == null ? 43 : $event.hashCode());
        final Object $timestamp = this.getTimestamp();
        result = result * PRIME + ($timestamp == null ? 43 : $timestamp.hashCode());
        final Object $sourceService = this.getSourceService();
        result = result * PRIME + ($sourceService == null ? 43 : $sourceService.hashCode());
        result = result * PRIME + this.getVersion();
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "DomainEvent(correlationId=" + this.getCorrelationId() + ", event=" + this.getEvent() + ", timestamp=" + this.getTimestamp() + ", sourceService=" + this.getSourceService() + ", version=" + this.getVersion() + ", data=" + this.getData() + ")";
    }
}