package dev.tt.poc.subscription.workflow;

import dev.tt.poc.subscription.model.EmailDetails;
import dev.tt.poc.subscription.model.WorkflowData;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

public class ProcessBWorkflowImpl extends BaseProcessWorkflow {
    @Override
    protected int defineExpectedCount() {
        return 5;  // Process B needs 5 messages
    }
}

