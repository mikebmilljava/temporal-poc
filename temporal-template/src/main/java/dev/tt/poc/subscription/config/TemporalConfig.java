package dev.tt.poc.subscription.config;

import io.temporal.common.metadata.*;
import io.temporal.spring.boot.autoconfigure.template.WorkersTemplate;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Configuration
public class TemporalConfig {
    @Bean
    public WorkerFactory workerFactory(WorkflowClient client) {
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker w = factory.newWorker("TASK_QUEUE");
        // register all workflow subclasses
        w.registerWorkflowImplementationTypes(
                ProcessAWorkflowImpl.class,
                ProcessBWorkflowImpl.class
        );
        w.registerActivitiesImplementations(processActivitiesBean());
        factory.start();
        return factory;
    }
}
