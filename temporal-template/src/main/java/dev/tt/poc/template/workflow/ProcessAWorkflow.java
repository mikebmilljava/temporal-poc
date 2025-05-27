package dev.tt.poc.template.workflow;

import dev.tt.poc.template.domain.InternalData;
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
