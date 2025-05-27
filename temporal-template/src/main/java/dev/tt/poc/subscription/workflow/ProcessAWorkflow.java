package dev.tt.poc.subscription.workflow;

import dev.tt.poc.subscription.domain.InternalData;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.SignalMethod;

@WorkflowInterface
public interface ProcessAWorkflow {

    @WorkflowMethod
    void run(String requestId, int expectedCount);

    @SignalMethod
    void signalInternalData(String requestId, InternalData data);
}
