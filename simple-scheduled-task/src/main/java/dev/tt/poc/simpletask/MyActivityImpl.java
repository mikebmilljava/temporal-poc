package dev.tt.poc.simpletask;

import io.temporal.spring.boot.ActivityImpl;
import org.springframework.stereotype.Component;

@ActivityImpl(taskQueues = "my-task-queue")
@Component
public class MyActivityImpl implements MyActivity {

    @Override
    public String execute(String input) {
        System.out.println("Activity executing with input: " + input);
        return "Activity Result for: " + input;
    }
}