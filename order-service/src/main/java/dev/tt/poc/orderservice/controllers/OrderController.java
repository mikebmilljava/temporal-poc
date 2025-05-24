package dev.tt.poc.orderservice.controllers;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tt.poc.workflow.OrderWorkflow;
import dev.tt.poc.workflow.WorkerHelper;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("orders")
public class OrderController {


    //create order here
    @PostMapping
    public void createOrder(@RequestBody CreateOrderRequest request) {
    	
    	ExecutorService executor = Executors.newFixedThreadPool(1);
    	for (int i = 0; i < 1; i++) {
    	    executor.submit(() -> {
    	    	double amount = 100 * request.items().size();

    	        var workflowClient = WorkerHelper.getWorkflowClient();

    	        WorkflowOptions options = WorkflowOptions.newBuilder()
    	                .setTaskQueue(WorkerHelper.ORDER_LIFECYCLE_WORKFLOW_TASK_QUEUE)
    	                .build();
    	        OrderWorkflow workflow = workflowClient.newWorkflowStub(OrderWorkflow.class, options);

    	        // Asynchronously start the workflow execution
    	        WorkflowClient.start(workflow::processOrder, request.customerId(), request.items(), amount);

    	    });
    	}
    	executor.shutdown();

        
    }
}


