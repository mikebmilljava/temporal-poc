package dev.tt.poc.subscription.service;

import dev.tt.poc.subscription.util.WorkflowUtils;
import dev.tt.poc.subscription.workflow.ProcessAWorkflow;
import dev.tt.poc.subscription.workflow.ProcessBWorkflow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;

@Service
public class WorkflowStarterService {

    private final WorkflowClient workflowClient;
    private final String taskQueue;

    // defaults to "DEFAULT_TASK_QUEUE" if not set in application properties
    public WorkflowStarterService(WorkflowClient workflowClient,
                                  @Value("${temporal.taskQueue:DEFAULT_TASK_QUEUE}") String taskQueue) {
        this.workflowClient = workflowClient;
        this.taskQueue = taskQueue;
    }

    public ProcessAWorkflow startProcessA(String requestId, int expectedCount) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(requestId)
                .build();
        ProcessAWorkflow stub = workflowClient.newWorkflowStub(ProcessAWorkflow.class, options);
        // non-blocking start; workflow executes asynchronously
        WorkflowUtils.startWorkflow(() -> stub.run(requestId, expectedCount));
        return stub;
    }

    public ProcessBWorkflow startProcessB(String requestId, int expectedCount) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(requestId)
                .build();
        ProcessBWorkflow stub = workflowClient.newWorkflowStub(ProcessBWorkflow.class, options);
        WorkflowUtils.startWorkflow(() -> stub.run(requestId, expectedCount));
        return stub;
    }
}

