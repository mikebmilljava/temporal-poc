package dev.tt.poc.subscription.activities;

import dev.tt.poc.subscription.domain.InternalData;
import dev.tt.poc.subscription.domain.ThirdPartyData;
import io.temporal.activity.ActivityInterface;
import java.util.List;

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
