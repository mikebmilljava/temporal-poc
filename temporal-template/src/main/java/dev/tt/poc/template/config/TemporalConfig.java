package dev.tt.poc.template.config;

import dev.tt.poc.template.activities.ProcessActivities;
import dev.tt.poc.template.workflow.ProcessAWorkflowImpl;
import dev.tt.poc.template.workflow.ProcessBWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name="temporal.server.enabled", havingValue="true", matchIfMissing=true)
@Configuration(proxyBeanMethods = false)
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
}
