package dev.tt.poc.orderservice.config;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import dev.tt.poc.orderservice.workflow.OrderActivityImpl;
import dev.tt.poc.workflow.OrderWorkflowImpl;
import dev.tt.poc.workflow.WorkerHelper;

@Configuration
public class TemporalConfig {

    @Autowired
    private OrderActivityImpl orderActivity;


    @PostConstruct
    public void startWorkers() {
        var stub = WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.newBuilder()
                .setEnableHttps(false)
                .setTarget("192.168.68.114:7233")
                .build());
        var client = WorkflowClient.newInstance(stub);

        var factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(WorkerHelper.ORDER_LIFECYCLE_WORKFLOW_TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(OrderWorkflowImpl.class);
        worker.registerActivitiesImplementations(orderActivity);
        factory.start();
    }
}