package dev.tt.poc.subscription.config;

import dev.tt.poc.subscription.activities.ProcessActivities;
import dev.tt.poc.subscription.activities.ProcessActivitiesImpl;
import dev.tt.poc.subscription.workflow.ProcessAWorkflowImpl;
import dev.tt.poc.subscription.workflow.ProcessBWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {
    @Bean
    public WorkerFactory workerFactory(WorkflowClient client,
                                       ProcessActivities processActivities) {
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker w = factory.newWorker("TEMPLATE_POC_TASK_QUEUE");
        w.registerWorkflowImplementationTypes(
                ProcessAWorkflowImpl.class,
                ProcessBWorkflowImpl.class
        );
        // register the activities implementation bean
        w.registerActivitiesImplementations(processActivities);
        factory.start();
        return factory;
    }

    @Bean
    public ProcessActivities processActivitiesBean(ProcessActivitiesImpl impl) {
        return impl;
    }
}
