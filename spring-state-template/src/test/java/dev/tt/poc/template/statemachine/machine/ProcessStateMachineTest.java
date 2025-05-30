package dev.tt.poc.template.statemachine.machine;

import dev.tt.poc.template.statemachine.config.StateMachineTestConfig;
import dev.tt.poc.template.statemachine.domain.InternalData;
import dev.tt.poc.template.statemachine.domain.ThirdPartyData;
import dev.tt.poc.template.statemachine.domain.ProcessContext;
import dev.tt.poc.template.statemachine.machine.Events;
import dev.tt.poc.template.statemachine.machine.States;
import dev.tt.poc.template.statemachine.service.ProcessActivities;
import dev.tt.poc.template.statemachine.service.ProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Instant;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(StateMachineTestConfig.class)
public class ProcessStateMachineTest {

    @Autowired
    private StateMachineFactory<States, Events> factory;

    @Autowired
    private ProcessActivities processActivities;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProcessService processService;

    @BeforeEach
    void setUp() {
        reset(processActivities, rabbitTemplate);
    }

    @Test
    void stateMachineHappyPath() {
        String requestId = "req1";
        int expectedSignalCount = 3;

        // Mock third-party fetch
        ThirdPartyData fakeData = ThirdPartyData.builder()
                .requestId(requestId)
                .fetchedAt(Instant.now())
                .sourceSystem("unit-test")
                .build();
        when(processActivities.fetchThirdParty(eq(requestId))).thenReturn(fakeData);

        // Build and start state machine
        StateMachine<States, Events> sm = factory.getStateMachine(requestId);
        ProcessContext ctx = ProcessContext.builder()
                .requestId(requestId)
                .expectedSignalCount(expectedSignalCount)
                .build();
        sm.getExtendedState().getVariables().put("processContext", ctx);
        sm.start();

        // Verify internal messages published
        verify(rabbitTemplate, timeout(1000).times(expectedSignalCount))
                .convertAndSend(eq("internal-exchange"), eq("internal-routing-key"), eq(requestId));

        // Send internal signals
        IntStream.range(0, expectedSignalCount)
                .forEach(i -> sm.sendEvent(Events.INTERNAL_SIGNAL));

        // Verify business logic completion
        verify(processActivities, timeout(1000).times(1))
                .completeBusinessLogic(eq(requestId), eq(fakeData), anyList());
    }

    @Test
    void processServiceOrchestration() {
        String requestId = "req2";
        int expectedSignalCount = 2;

        // Mock third-party fetch
        ThirdPartyData fakeData = ThirdPartyData.builder()
                .requestId(requestId)
                .fetchedAt(Instant.now())
                .sourceSystem("unit-test")
                .build();
        when(processActivities.fetchThirdParty(eq(requestId))).thenReturn(fakeData);

        // Start via ProcessService
        StateMachine<States, Events> sm = processService
                .startProcess(requestId, expectedSignalCount);

        // Publish internal messages
        verify(rabbitTemplate, timeout(1000).times(expectedSignalCount))
                .convertAndSend(eq("internal-exchange"), eq("internal-routing-key"), eq(requestId));

        // Signal internal data events
        IntStream.range(0, expectedSignalCount)
                .forEach(i -> sm.sendEvent(Events.INTERNAL_SIGNAL));

        // Verify business logic completion
        verify(processActivities, timeout(1000).times(1))
                .completeBusinessLogic(eq(requestId), eq(fakeData), anyList());
    }
}
