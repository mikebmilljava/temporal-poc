package dev.tt.poc.orderservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tt.poc.orderservice.workflow.OrderWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;

@RestController
@RequestMapping("orders")
public class OrderController {

	 @Autowired
	    WorkflowClient client;

    //create order here
    @PostMapping
    public void createOrder(@RequestBody CreateOrderRequest request) {
    	   WorkflowOptions options = WorkflowOptions.newBuilder()
                   .setWorkflowId(UUID.randomUUID().toString())
                   .setTaskQueue("order-workflow-taskqueue")
                   .build();

    	   OrderWorkflow workflow = client.newWorkflowStub(OrderWorkflow.class, options);
           double amount = 100 * request.items().size();
           WorkflowClient.start(workflow::processOrder, request.customerId(), request.items(), amount);
           
       
        
    }
}


