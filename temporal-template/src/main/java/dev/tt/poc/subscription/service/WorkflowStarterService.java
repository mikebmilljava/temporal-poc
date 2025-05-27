package dev.tt.poc.subscription.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import com.example.temporaltemplatepoc.workflow.ProcessAWorkflow;
import com.example.temporaltemplatepoc.workflow.ProcessBWorkflow;

@Service
public class WorkflowStarterService {

    private final WorkflowClient workflowClient;
    private final String taskQueue;

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
        WorkflowClient.start(() -> stub.runProcessA(requestId, expectedCount));
        return stub;
    }

    public ProcessBWorkflow startProcessB(String requestId, int expectedCount) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(requestId)
                .build();
        ProcessBWorkflow stub = workflowClient.newWorkflowStub(ProcessBWorkflow.class, options);
        WorkflowClient.start(() -> stub.runProcessB(requestId, expectedCount));
        return stub;
    }
}

