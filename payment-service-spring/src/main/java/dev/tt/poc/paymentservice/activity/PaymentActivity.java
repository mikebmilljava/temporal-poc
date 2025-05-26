package dev.tt.poc.paymentservice.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PaymentActivity {

    @ActivityMethod
    void processPayment(Long orderId, double amount);

}
