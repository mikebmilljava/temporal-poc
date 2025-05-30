package dev.tt.poc.template;

import dev.tt.poc.template.statemachine.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.tt.poc.template.statemachine")
public class ProcessStateMachineRunner implements CommandLineRunner {

    private final ProcessService processService;

    @Autowired
    public ProcessStateMachineRunner(ProcessService processService) {
        this.processService = processService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProcessStateMachineRunner.class, args);
    }

    @Override
    public void run(String... args) {
        String requestId = args.length > 0 ? args[0] : "default-request";
        int expectedSignalCount = 3;
        if (args.length > 1) {
            try {
                expectedSignalCount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid signal count argument, defaulting to 3");
            }
        }

        System.out.printf("Starting process for requestId='%s' with expected signals=%d%n",
                requestId, expectedSignalCount);

        processService.startProcess(requestId, expectedSignalCount);
    }
}

