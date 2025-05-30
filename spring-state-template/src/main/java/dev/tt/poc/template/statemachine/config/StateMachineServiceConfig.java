package dev.tt.poc.template.statemachine.config;

import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
public class StateMachineServiceConfig {

    @Bean
    public StateMachineService<States, Events> stateMachineService(
            StateMachineFactory<States, Events> factory,
            StateMachineRuntimePersister<States, Events, String> persister) {
        return new DefaultStateMachineService<>(factory, persister);
    }
}
