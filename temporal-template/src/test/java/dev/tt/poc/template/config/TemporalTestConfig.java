package dev.tt.poc.template.config;

import dev.tt.poc.template.activities.ProcessActivitiesImpl;
import dev.tt.poc.template.workflow.ProcessAWorkflowImpl;
import dev.tt.poc.template.workflow.ProcessBWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TemporalTestConfig {

    @Bean
    public TestWorkflowEnvironment testEnv() {
        // newInstance() already spins up the in-proc gRPC server
        return TestWorkflowEnvironment.newInstance();
    }

    @Bean
    @Primary
    public WorkflowServiceStubs serviceStubs(TestWorkflowEnvironment env) {
        return env.getWorkflowServiceStubs();
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs stubs) {
        return WorkflowClient.newInstance(stubs);
    }

    @Bean
    @Primary
    public WorkerFactory workerFactory(
            TestWorkflowEnvironment env,
            ProcessActivitiesImpl activities  // your activities impl
    ) {
        // grab the factory
        WorkerFactory factory = env.getWorkerFactory();

        // create your worker off of that factory *before* you start it
        Worker worker = factory.newWorker("TEMPLATE_POC_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(ProcessAWorkflowImpl.class, ProcessBWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities);

        // now actually start polling
        factory.start();
        return factory;
    }

    @Bean
    @Primary
    public ProcessActivitiesImpl processActivities(
            RestTemplate restTemplate,
            RabbitTemplate rabbitTemplate
    ) {
        return new ProcessActivitiesImpl(restTemplate, rabbitTemplate);
    }
}