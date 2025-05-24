package dev.tt.poc.subscription.workflow;


import dev.tt.poc.subscription.model.EmailDetails;
import dev.tt.poc.subscription.model.WorkflowData;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SendEmailWorkflow {

    @WorkflowMethod
    public void run(WorkflowData data);

    @QueryMethod
    public EmailDetails details();
}