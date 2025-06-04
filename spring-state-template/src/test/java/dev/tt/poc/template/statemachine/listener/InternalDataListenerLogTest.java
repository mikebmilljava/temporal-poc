package dev.tt.poc.template.statemachine.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import dev.tt.poc.template.statemachine.config.RabbitConfig;
import dev.tt.poc.template.statemachine.domain.InternalData;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InternalDataListenerLogTest {

    @Mock
    StateMachineService<States, Events> smService;

    @Mock
    RabbitTemplate rabbitTemplate;

    @InjectMocks InternalDataListener listener;

    @Mock StateMachine<States, Events> stateMachine;
    @Mock org.springframework.statemachine.state.State<States, Events> currentState;

    private static final String MACHINE_ID = "request-123";

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 1) Configure StateMachineService to return our mock stateMachine
        when(smService.acquireStateMachine(eq(MACHINE_ID))).thenReturn(stateMachine);

        // 2) Attach a ListAppender to capture logs from InternalDataListener’s logger
        Logger logger = (Logger) LoggerFactory.getLogger(InternalDataListener.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    void onRawInternalMessage_logsAndSendsToInternalSignalExchange() {
        //
        // Given
        //
        InternalData data = makeInternalData();

        //
        // When
        //
        listener.onRawInternalMessage(data);

        boolean found = listAppender.list.stream()
                .anyMatch(evt ->
                        evt.getLevel() == Level.INFO &&
                                evt.getFormattedMessage().contains("Message received with " + MACHINE_ID)
                );

        //
        // Then
        //
        assertTrue(found, "Expected an INFO log containing the requestId");

        verify(rabbitTemplate, times(1)).convertAndSend(
                RabbitConfig.INTERNAL_SIGNAL_DIRECT_EXCHANGE,
                RabbitConfig.INTERNAL_SIGNAL_ROUTING_KEY,
                data
        );

        verifyNoInteractions(smService);
    }

    private InternalData makeInternalData() {
        return InternalData.builder()
                .requestId(MACHINE_ID)
                .dataKey("key")
                .dataValue("value")
                .receivedAt(Instant.now())
                .metadata(Map.of())
                .build();
    }

}
