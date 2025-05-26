package dev.tt.poc.simpletask;

import io.temporal.activity.Activity;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.QueryMethod;

@WorkflowInterface
public interface SimpleQueryWorkflow {

    @WorkflowMethod
    String myWorkflow(String input);

    @SignalMethod
    void mySignal(String signalInput);

    @QueryMethod
    String myQuery();
}