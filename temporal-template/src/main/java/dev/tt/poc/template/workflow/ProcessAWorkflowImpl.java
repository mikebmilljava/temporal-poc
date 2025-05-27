package dev.tt.poc.template.workflow;

public class ProcessAWorkflowImpl extends BaseProcessWorkflow {
    @Override
    protected int defineExpectedCount() {
        return 3;  // Process A expects 3 internal messages
    }
}
