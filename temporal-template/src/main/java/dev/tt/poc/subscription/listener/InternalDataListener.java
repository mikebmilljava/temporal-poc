package dev.tt.poc.subscription.listener;

import dev.tt.poc.subscription.model.EmailDetails;
import dev.tt.poc.subscription.model.WorkflowData;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

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
}
