package dev.tt.poc.shippingservice.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ShippingActivity {

    @ActivityMethod
    void processShipment(Long orderId);

}
