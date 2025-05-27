package dev.tt.poc.inventory.command;

public interface EventCommand<T> {

    void execute(DomainEvent<T> event);
    void undo();
}