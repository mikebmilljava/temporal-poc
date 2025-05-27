package dev.tt.poc.subscription.util;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import java.util.Objects;
import java.util.UUID;

public final class WorkflowUtils {

    public static final String DEFAULT_TASK_QUEUE = "TEMPLATE_POC_TASK_QUEUE";

    private WorkflowUtils() {
        throw new AssertionError("No instances");
    }

    public static String generateWorkflowId(String prefix) {
        Objects.requireNonNull(prefix, "prefix");
        return prefix + "-" + UUID.randomUUID();
    }

    public static <T> T newWorkflowStub(
            WorkflowClient client,
            Class<T> workflowCls,
            String workflowId) {

        Objects.requireNonNull(client, "client");
        Objects.requireNonNull(workflowCls, "workflowCls");
        Objects.requireNonNull(workflowId, "workflowId");

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(workflowId)
                .setTaskQueue(DEFAULT_TASK_QUEUE)
                .build();
        return client.newWorkflowStub(workflowCls, options);
    }

    public static void startWorkflow(WorkflowStarter starter) {
        Objects.requireNonNull(starter, "starter");
        WorkflowClient.start(starter::start);
    }

    @FunctionalInterface
    public interface WorkflowStarter {
        void start();
    }
}