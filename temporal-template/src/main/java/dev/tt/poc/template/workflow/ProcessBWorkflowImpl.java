package dev.tt.poc.template.workflow;

public class ProcessBWorkflowImpl extends BaseProcessWorkflow {
    @Override
    protected int defineExpectedCount() {
        return 5;  // Process B needs 5 messages
    }
}

