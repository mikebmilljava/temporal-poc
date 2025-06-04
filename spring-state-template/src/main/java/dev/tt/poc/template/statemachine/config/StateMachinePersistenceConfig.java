package dev.tt.poc.template.statemachine.config;

import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.machine.Events;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
//import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
//import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.StateMachineInterceptor;

@Configuration
public class StateMachinePersistenceConfig {

    @Bean
    public StateMachineRuntimePersister<States, Events, String> runtimePersister() {
        return new StateMachineRuntimePersister<>() {
            @Override
            public StateMachineInterceptor<States, Events> getInterceptor() {
                return null;
            }

            @Override
            public void write(StateMachineContext<States, Events> stateMachineContext, String s) throws Exception {

            }

            @Override
            public StateMachineContext<States, Events> read(String contextObj) {
                return null;  // always start fresh
            }
        };
    }

    // :todo
    //@Bean
    //public StateMachineRuntimePersister<States, Events, String> runtimePersister(
    //        JpaStateMachineRepository repository) {
    //    return new JpaPersistingStateMachineInterceptor<>(repository);
    //}
}


