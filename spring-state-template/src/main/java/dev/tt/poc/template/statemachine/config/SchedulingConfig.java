package dev.tt.poc.template.statemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulingConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // optional: set pool size, thread name prefix, etc.
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("sm-timeout-");
        scheduler.initialize();
        return scheduler;
    }
}