package dev.tt.poc.subscription.listener;

import dev.tt.poc.subscription.domain.InternalData;
import dev.tt.poc.subscription.dto.InternalDataMessage;
import dev.tt.poc.subscription.workflow.ProcessAWorkflow;
import io.temporal.client.WorkflowClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class InternalDataListener {
    private final WorkflowClient client;

    public InternalDataListener(WorkflowClient client) {
        this.client = client;
    }

    @RabbitListener(queues="internal-data-queue")
    public void onMessage(InternalDataMessage msg) {
        // Lookup the workflow stub & send signal
        ProcessAWorkflow stub = client.newWorkflowStub(
                ProcessAWorkflow.class,
                msg.getRequestId());  // same workflowId used at start
        stub.signalInternalData(msg.getRequestId(),
                mapToInternalData(msg));
    }

    private InternalData mapToInternalData(InternalDataMessage msg) {
        return InternalData.builder().dataKey(msg.getDataKey()).dataValue(msg.getDataValue()).requestId(msg.getRequestId()).build();
    }
}
