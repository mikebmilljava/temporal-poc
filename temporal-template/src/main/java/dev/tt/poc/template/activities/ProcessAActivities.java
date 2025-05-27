package dev.tt.poc.template.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ProcessAActivities extends ProcessActivities {
    // If Process A ever needs extra methods beyond the common ones,
    // define them here. For now, it reuses the ProcessActivities contract.
}

