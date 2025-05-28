package dev.tt.poc.template.workflow;

import dev.tt.poc.template.TemporalTemplatePocApplication;
import dev.tt.poc.template.activities.ProcessActivitiesImpl;
import dev.tt.poc.template.config.TemporalTestConfig;
import dev.tt.poc.template.domain.InternalData;
import dev.tt.poc.template.domain.ThirdPartyData;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.spring.boot.autoconfigure.RootNamespaceAutoConfiguration;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.WorkerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {
        TemporalTemplatePocApplication.class,
        TemporalTestConfig.class
},
        properties = {
        "temporal.server.enabled=false",
                "spring.rabbitmq.listener.simple.auto-startup=false",
                "temporal.service.address=none"})

@EnableAutoConfiguration(exclude={ DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class, RootNamespaceAutoConfiguration.class})
class ProcessXWorkflowTest {

    @Autowired
    private WorkflowClient client;

    @Autowired
    private WorkerFactory factory;

    @Autowired
    private TestWorkflowEnvironment testEnv;

    @SpyBean
    private ProcessActivitiesImpl activitiesSpy;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    RabbitTemplate rabbitTemplate;


    @BeforeEach
    void setUp() {
        //factory.getWorker("TEMPLATE_POC_TASK_QUEUE").registerActivitiesImplementations(activitiesSpy);
    }

    @Test
    void processAHappyPath() {
        // 1) Arrange: mock the 3rd-party fetch
        ThirdPartyData fakeData = ThirdPartyData.builder().fetchedAt(Instant.now()).requestId("reqX").sourceSystem("sourceSystem").build();
        when(activitiesSpy.fetchThirdParty("reqX")).thenReturn(fakeData);

        // 2) Start the workflow asynchronously
        ProcessAWorkflow stub = client.newWorkflowStub(
                ProcessAWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("TEMPLATE_POC_TASK_QUEUE")
                        .setWorkflowId("reqX")
                        .build()
        );
        WorkflowClient.start(() -> stub.run("reqX",3));

        // 3) Signal the workflow the expected number of times
        int expectedCount = 3;
        IntStream.range(0, expectedCount).forEach(i ->
                stub.signalInternalData("reqX", InternalData.builder().receivedAt(Instant.now()).requestId("requestId").dataKey("dataKey").dataValue("dataValue").build())
        );

        // 4) Verify that completeBusinessLogic(...) was called exactly once
        verify(activitiesSpy, timeout(5_000).times(1))
                .completeBusinessLogic(eq("reqX"), eq(fakeData), anyList());
    }
}

