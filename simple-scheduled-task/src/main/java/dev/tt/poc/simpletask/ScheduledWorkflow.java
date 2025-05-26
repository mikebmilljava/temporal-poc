package dev.tt.poc.simpletask;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import io.temporal.client.WorkflowOptions;
import io.temporal.client.schedules.Schedule;
import io.temporal.client.schedules.ScheduleActionStartWorkflow;
import io.temporal.client.schedules.ScheduleClient;
import io.temporal.client.schedules.ScheduleHandle;
import io.temporal.client.schedules.ScheduleOptions;
import io.temporal.client.schedules.ScheduleSpec;
import io.temporal.common.RetryOptions;
import jakarta.annotation.PostConstruct;

@Component
public class ScheduledWorkflow {

    @Autowired
    private ScheduleClient scheduleClient;

    @PostConstruct
    public void createSchedule() {
    
    	
    	Schedule schedule =
    		    Schedule.newBuilder()
    		        .setAction(
    		            ScheduleActionStartWorkflow.newBuilder()
    		                .setWorkflowType(SimpleQueryWorkflow.class)
    		                .setArguments("World")
    		                
    		                .setOptions(
    		                    WorkflowOptions.newBuilder()
    		                    
    		                        .setWorkflowId(UUID.randomUUID().toString())
    		                        .setWorkflowTaskTimeout(Duration.ofSeconds(15))
    		                        .setTaskQueue("my-task-queue")
														.setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(5)
																.setInitialInterval(Duration.ofSeconds(1))
																.setBackoffCoefficient(2.0).build())
						        		                        .setWorkflowRunTimeout(Duration.ofMinutes(10))
						        		                        .setWorkflowTaskTimeout(Duration.ofSeconds(30))
    		                        .build())
    		                .build())
    		        .setSpec(ScheduleSpec.newBuilder().build())
    		        .build();

    		// Create a schedule on the server
    		ScheduleHandle handle =
    		    scheduleClient.createSchedule("Simple Schedule", schedule, ScheduleOptions.newBuilder().build());
    		
    		
    }
}
