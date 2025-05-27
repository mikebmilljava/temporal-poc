package dev.tt.poc.subscription.workflow;

import dev.tt.poc.subscription.activities.ProcessAActivities;
import dev.tt.poc.subscription.domain.InternalData;
import dev.tt.poc.subscription.domain.ThirdPartyData;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.worker.WorkerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProcessXWorkflowTest {

    @Autowired
    private WorkflowClient client;

    @Autowired
    private WorkerFactory factory;

    @MockBean
    private ProcessAActivities activities;

    @Test
    void processXHappyPath() {
        // 1) Arrange: mock the 3rd-party fetch
        ThirdPartyData fakeData = ThirdPartyData.builder().requestId("reqX").sourceSystem("sourceSystem").build();
        when(activities.fetchThirdParty("reqX")).thenReturn(fakeData);

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
                stub.signalInternalData("reqX", InternalData.builder().build())
        );

        // 4) Verify that completeBusinessLogic(...) was called exactly once
        verify(activities, timeout(5_000).times(1))
                .completeBusinessLogic(eq("reqX"), eq(fakeData), anyList());
    }
}

