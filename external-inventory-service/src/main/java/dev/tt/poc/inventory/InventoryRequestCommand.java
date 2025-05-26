package dev.tt.poc.inventory;

import java.util.Map;

import org.springframework.stereotype.Service;

import dev.tt.poc.InventoryRequest;
import dev.tt.poc.inventory.command.DomainEvent;
import dev.tt.poc.inventory.command.EventCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("INVENTORY_REQUEST")
public class InventoryRequestCommand implements EventCommand<InventoryRequest> {

    @Override
    public void undo() {

    }

	@Override
	public void execute(DomainEvent<InventoryRequest> event) {
		 log.info(String.valueOf(event));
		
	}
    
}