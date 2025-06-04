package dev.tt.poc.template.statemachine.listener;

import dev.tt.poc.template.statemachine.config.ProcessAStateMachineConfig;
import dev.tt.poc.template.statemachine.config.RabbitConfig;
import dev.tt.poc.template.statemachine.domain.InternalData;
import dev.tt.poc.template.statemachine.domain.ProcessContext;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import org.springframework.statemachine.service.StateMachineService;

import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@RabbitListenerTest
@SpringBootTest(
        properties = "spring.main.allow-bean-definition-overriding=true"
)
@Import({
        RabbitConfig.class,
        InternalDataListenerIntegrationTest.JsonRabbitConfig.class,
        InternalDataListenerIntegrationTest.RawRabbitConfig.class,
        InternalDataListener.class,
        InternalDataListenerIntegrationTest.TestStateMachineConfig.class,
        ProcessAStateMachineConfig.class
})
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InternalDataListenerIntegrationTest {

    // Start RabbitMQContainer early so it's running before Spring context binds properties
    @Container
    static RabbitMQContainer rabbitContainer =
            new RabbitMQContainer("rabbitmq:3.9.5-management");

    // Register spring.rabbitmq.addresses dynamically from the running container
    @DynamicPropertySource
    static void setRabbitProperties(DynamicPropertyRegistry registry) {
        rabbitContainer.start();
        registry.add("spring.rabbitmq.addresses", rabbitContainer::getAmqpUrl);
    }

    // Use the regular RabbitTemplate instead of TestRabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private StateMachineService<States, ?> smService;

    @Autowired
    private StateMachineFactory<States, ?> factory;

    private static final String REQUEST_ID = "req-1";

    private InternalData makeInternalData() {
        return InternalData.builder()
                .requestId(REQUEST_ID)
                .dataKey("testKey")
                .dataValue("testValue")
                .receivedAt(Instant.now())
                .metadata(Map.of("foo", "bar"))
                .build();
    }

    @TestConfiguration
    static class JsonRabbitConfig {
        @Bean
        @Primary
        public MessageConverter jackson2JsonMessageConverter() {
            return new Jackson2JsonMessageConverter();
        }
    }

    /**
     * TestConfiguration to declare the "raw" exchange/queue/binding so that
     * Spring auto‐creates them before any @RabbitListener tries to consume.
     */
    @TestConfiguration
    static class RawRabbitConfig {
        @Bean
        public DirectExchange rawInternalExchange() {
            return new DirectExchange("raw-internal-exchange");
        }

        @Bean
        public Queue rawInternalQueue() {
            return new Queue("raw-internal-queue");
        }

        @Bean
        public Binding rawInternalBinding(Queue rawInternalQueue, DirectExchange rawInternalExchange) {
            return BindingBuilder
                    .bind(rawInternalQueue)
                    .to(rawInternalExchange)
                    .with("raw.internal.routing-key");
        }
    }

    @TestConfiguration
    static class TestStateMachineConfig {

        @Bean
        @Primary
        public StateMachineRuntimePersister<States, Events, String> inMemoryPersister() {
            return new StateMachineRuntimePersister<>() {
                private final ConcurrentMap<String, StateMachineContext<States, Events>> storage =
                        new ConcurrentHashMap<>();

                @Override
                public StateMachineInterceptor<States, Events> getInterceptor() {
                    return new StateMachineInterceptorAdapter<States, Events>() {
                        @Override
                        public void preStateChange(State<States, Events> state,
                                                   Message<Events> message,
                                                   Transition<States, Events> transition,
                                                   StateMachine<States, Events> stateMachine,
                                                   StateMachine<States, Events> rootStateMachine) {
                            StateMachineContext<States, Events> ctx =
                                    new DefaultStateMachineContext<States, Events>(
                                            state.getId(),
                                            null,
                                            null,
                                            stateMachine.getExtendedState()
                                    );
                            try {
                                if (ctx != null && stateMachine != null && stateMachine.getId() != null) {
                                    write(ctx, stateMachine.getId());
                                }
                            } catch (Exception e) {
                                throw new IllegalStateException("Failed to write state machine context", e);
                            }
                        }
                    };
                }

                @Override
                public void write(StateMachineContext<States, Events> context, String key) {
                    storage.put(key, context);
                }

                @Override
                public StateMachineContext<States, Events> read(String key) {
                    return storage.get(key);
                }
            };
        }

        @Bean
        @Primary
        public StateMachineFactory<States, Events> testStateMachineFactory(
                StateMachineRuntimePersister<States, Events, String> inMemoryPersister
        ) {
            return new StateMachineFactory<>() {
                @SneakyThrows
                @Override
                public StateMachine<States, Events> getStateMachine(String machineId) {
                    StateMachineBuilder.Builder<States, Events> builder = StateMachineBuilder.builder();

                    builder.configureConfiguration()
                            .withConfiguration()
                            .machineId(machineId)
                            .and()
                            .withPersistence()
                            .runtimePersister(inMemoryPersister);

                    builder.configureStates()
                            .withStates()
                            .initial(States.START)
                            .state(States.FETCH_THIRD_PARTY)
                            .state(States.PUBLISH_INTERNAL)
                            .state(States.WAIT_FOR_INTERNAL)
                            .state(States.TIMEOUT)
                            .state(States.COMPLETE)
                            .end(States.END);

                    builder.configureTransitions()
                            .withExternal()
                            .source(States.START).target(States.WAIT_FOR_INTERNAL)
                            .event(Events.INTERNAL_SIGNAL)
                            .and()
                            .withExternal()
                            .source(States.START).target(States.PUBLISH_INTERNAL)
                            .event(Events.THIRD_PARTY_FETCHED)
                            .and()
                            .withExternal()
                            .source(States.WAIT_FOR_INTERNAL).target(States.TIMEOUT)
                            .event(Events.INTERNAL_TIMEOUT)
                            .and()
                            .withExternal()
                            .source(States.WAIT_FOR_INTERNAL).target(States.COMPLETE)
                            .event(Events.INTERNAL_COMPLETE)
                            .and()
                            .withExternal()
                            .source(States.COMPLETE).target(States.END)
                            .event(Events.BUSINESS_COMPLETE);

                    // 2) all states/transitions were already wired when "testStateMachineBuilder" was defined as a bean
                    StateMachine<States, Events> sm = builder.build();
                    sm.addStateListener(new StateMachineListenerAdapter<States,Events>() {
                        @Override
                        public void stateChanged(State<States,Events> from, State<States,Events> to) {
                            System.out.println("Transition: " +
                                    (from == null ? "NONE" : from.getId()) + " -> " + to.getId());
                        }
                    });
                    return sm;
                }
                @Override
                public StateMachine<States, Events> getStateMachine() {
                    return getStateMachine(UUID.randomUUID().toString());
                }
                @Override
                public StateMachine<States, Events> getStateMachine(UUID uuid) {
                    return getStateMachine(uuid.toString());
                }
            };
        }

        @Bean
        @Primary
        public StateMachineService<States, Events> testStateMachineService(
                StateMachineFactory<States, Events> testStateMachineFactory,
                StateMachineRuntimePersister<States, Events, String> inMemoryPersister
        ) {
            return new DefaultStateMachineService<>(testStateMachineFactory, inMemoryPersister);
        }

    }

    @Test
    void rawToInternalSignalHappyPath() throws InterruptedException {
        // Build & start a fresh StateMachine with expectedSignalCount = 1
        int expectedSignalCount = 1;
        StateMachine<States, Events> sharedSm = (StateMachine<States, Events>) smService.acquireStateMachine(REQUEST_ID);

        ProcessContext ctx = ProcessContext.builder()
                .requestId(REQUEST_ID)
                .expectedSignalCount(expectedSignalCount)
                .build();
        sharedSm.getExtendedState().getVariables().put("processContext", ctx);
        sharedSm.startReactively().block();
        sharedSm.sendEvent(Events.INTERNAL_SIGNAL);
        // (Optionally release immediately, though not strictly necessary here)
        smService.releaseStateMachine(REQUEST_ID);

        // 5) Publish an InternalData to the "raw-internal-exchange" using RabbitTemplate
        InternalData data = makeInternalData();
        rabbitTemplate.convertAndSend(
                "raw-internal-exchange",
                "raw.internal.routing-key",
                data
        );

        // Wait briefly, allowing listener and state-machine to process
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .until(() -> {
                    @SuppressWarnings("unchecked")
                    StateMachine<States, Events> checkSm =
                            (StateMachine<States, Events>) smService.acquireStateMachine(REQUEST_ID);
                    boolean reached = checkSm.getState().getId() == States.WAIT_FOR_INTERNAL;
                    smService.releaseStateMachine(REQUEST_ID);
                    return reached;
                });

        sharedSm = (StateMachine<States, Events>) smService.acquireStateMachine(REQUEST_ID);
        sharedSm.sendEvent(Events.INTERNAL_COMPLETE);

        ProcessContext storedCtx = sharedSm.getExtendedState().get("processContext", ProcessContext.class);
        assertNotNull(storedCtx, "ProcessContext should exist after listener runs");
        assertEquals(1, storedCtx.getReceivedData().size(), "Should have recorded one InternalData");
        assertEquals(data, storedCtx.getReceivedData().get(0));
        // Because expectedSignalCount = 1, the machine should have moved to COMPLETE
        assertEquals(States.COMPLETE, sharedSm.getState().getId(), "StateMachine should be in COMPLETE state");
    }
}
