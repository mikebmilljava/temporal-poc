package dev.tt.poc.template.statemachine.service;

import dev.tt.poc.template.statemachine.domain.InternalData;
import dev.tt.poc.template.statemachine.domain.ThirdPartyData;

import java.util.List;

public interface ProcessActivities {

    ThirdPartyData fetchThirdParty(String requestId);

    void completeBusinessLogic(
            String requestId,
            ThirdPartyData fetchedData,
            List<InternalData> internalDataList
    );
}