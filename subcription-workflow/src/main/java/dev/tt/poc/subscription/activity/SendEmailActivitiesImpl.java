package dev.tt.poc.subscription.activity;


import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import dev.tt.poc.subscription.model.EmailDetails;
import io.temporal.spring.boot.ActivityImpl;

@Component
@ActivityImpl(workers = "send-email-worker")
public class SendEmailActivitiesImpl implements SendEmailActivities {
    @Override
    public String sendEmail(EmailDetails details) {
        String response = MessageFormat.format(
            "Sending email to {0} with message: {1}, count: {2}",
            details.email, details.message, details.count);
        System.out.println(response);
        return "success";
    }
}