package dev.tt.poc.inventory.command;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class EventManager {
    private final EventCommandRegistry commandRegistry;

    public EventManager(EventCommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @RabbitListener(queues = "inventory-queue")
    void handleDomainEvent(DomainEvent<?> event){
        commandRegistry.resolve(event.getEvent())
                .ifPresent(command -> executeCommand(command, event));
    }

  
    @SuppressWarnings("unchecked")
    private <T> void executeCommand(EventCommand<T> command, DomainEvent<?> event) {
        command.execute((DomainEvent<T>) event);
    }
}
