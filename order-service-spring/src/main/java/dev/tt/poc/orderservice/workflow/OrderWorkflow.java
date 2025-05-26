package dev.tt.poc.orderservice.workflow;

import java.util.Map;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface OrderWorkflow {

    @WorkflowMethod
    void processOrder(String customerId, Map<Long, Integer> orderLines, double amount);
}
