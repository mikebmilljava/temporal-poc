package dev.tt.poc.template.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ProcessBActivities extends ProcessActivities {
    // E.g. if Process B needs x‐specific I/O:
    // ThirdPartyData fetchAnotherThirdParty(String reqId);
}

