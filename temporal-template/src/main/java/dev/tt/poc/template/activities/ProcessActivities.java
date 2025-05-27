package dev.tt.poc.template.activities;

import dev.tt.poc.template.domain.InternalData;
import dev.tt.poc.template.domain.ThirdPartyData;
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
