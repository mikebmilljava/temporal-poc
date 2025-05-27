package dev.tt.poc.inventory.command;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class EventCommandRegistry {
    private final Map<String, EventCommand<?>> handlers;

    public EventCommandRegistry(Map<String, EventCommand<?>> handlers) {
        this.handlers = handlers;
    }

    public Optional<EventCommand<?>> resolve(String eventName){
        return Optional.ofNullable(handlers.get(eventName));
    }
}