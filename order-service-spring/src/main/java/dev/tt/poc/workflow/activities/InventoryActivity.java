package dev.tt.poc.workflow.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface InventoryActivity {

    @ActivityMethod
    void processInventory(String workflowId, Long orderId, double amount);

}
