package dev.tt.poc.template.statemachine.config;

import dev.tt.poc.template.statemachine.config.RabbitConfig;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.service.ProcessActivities;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.machine.Events;

import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
@EnableStateMachineFactory
@Import({
        BaseStateMachineConfig.class,
        ProcessAStateMachineConfig.class,
        StateMachinePersistenceConfig.class,
        RabbitConfig.class
})
public class StateMachineTestConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        return scheduler;
    }

    @Bean
    public RestTemplate restTemplate() {
        return Mockito.mock(RestTemplate.class);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return Mockito.mock(RabbitTemplate.class);
    }

    @Bean
    @Primary
    public ProcessActivities processActivities() {
        return Mockito.mock(ProcessActivities.class);
    }

    @Bean
    @Primary
    public StateMachineRuntimePersister<States, Events, String> runtimePersister() {
        return new StateMachineRuntimePersister<States, Events, String>() {
            @Override
            public StateMachineInterceptor<States, Events> getInterceptor() {
                return null;
            }

            @Override
            public void write(StateMachineContext<States, Events> stateMachineContext, String s) throws Exception {

            }

            @Override
            public StateMachineContext<States, Events> read(String contextObj) {
                return null;
            }
        };
    }
}
