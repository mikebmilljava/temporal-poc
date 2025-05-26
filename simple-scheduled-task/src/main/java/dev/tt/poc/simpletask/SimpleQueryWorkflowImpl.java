package dev.tt.poc.simpletask;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;

@WorkflowImpl(taskQueues = "my-task-queue")
public class SimpleQueryWorkflowImpl implements SimpleQueryWorkflow {

    private final MyActivity activity = Workflow.newActivityStub(MyActivity.class,ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(10)).build());
    private String greeting = "Initial Greeting";

    @Override
    public String myWorkflow(String input) {
        String result = activity.execute(input);
        greeting = "Workflow executed with result: " + result;
        System.out.println("Workflow executed with input: " + input);
        return greeting;
    }

    @Override
    public void mySignal(String signalInput) {
        greeting = "Signal received: " + signalInput;
    }

    @Override
    public String myQuery() {
        return greeting;
    }
}