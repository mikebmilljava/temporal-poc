package dev.tt.poc.template.workflow;

import dev.tt.poc.template.activities.ProcessAActivities;
import dev.tt.poc.template.domain.ThirdPartyData;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class ProcessAWorkflowImpl extends BaseProcessWorkflow implements ProcessAWorkflow  {

    private static final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(1))
            .build();

    private final ProcessAActivities activities =
            Workflow.newActivityStub(ProcessAActivities.class, options);

    @Override
    protected int defineExpectedCount() {
        return 3;  // Process A expects 3 internal messages
    }

    @Override
    public void run(String requestId, int expectedCount) {
        ThirdPartyData tp = activities.fetchThirdParty(requestId);
        activities.publishInternalRequests(requestId);
        waitFor(expectedCount);
        activities.completeBusinessLogic(requestId, tp, this.internalDatas);
    }

}
