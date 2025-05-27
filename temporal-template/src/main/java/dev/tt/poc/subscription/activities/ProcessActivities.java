package dev.tt.poc.subscription.activities;

import dev.tt.poc.subscription.model.EmailDetails;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ProcessActivities {
    ThirdPartyData fetchThirdParty(String requestId);
    void publishInternalRequests(String requestId);
    void completeBusinessLogic(
            String requestId,
            ThirdPartyData tpData,
            List<InternalData> internalDatas
    );
}
