package dev.tt.poc;

import dev.tt.poc.inventory.command.EventCommandRegistry;

//@Component
//@Slf4j
public class RabbitMQConsumer {

	private final EventCommandRegistry eventCommandRegistry;
	
	public RabbitMQConsumer(EventCommandRegistry eventCommandRegistry) {
		this.eventCommandRegistry = eventCommandRegistry;
		
	}

	
}
