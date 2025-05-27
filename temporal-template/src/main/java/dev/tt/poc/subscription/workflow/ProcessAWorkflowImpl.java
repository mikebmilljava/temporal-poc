package dev.tt.poc.subscription.workflow;

import dev.tt.poc.subscription.model.EmailDetails;
import dev.tt.poc.subscription.model.WorkflowData;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

public class ProcessAWorkflowImpl extends BaseProcessWorkflow {
    @Override
    protected int defineExpectedCount() {
        return 3;  // Process A expects 3 internal messages
    }
}
