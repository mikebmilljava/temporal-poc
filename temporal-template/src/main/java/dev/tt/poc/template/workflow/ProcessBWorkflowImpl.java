package dev.tt.poc.template.workflow;

import dev.tt.poc.template.activities.ProcessBActivities;
import dev.tt.poc.template.domain.ThirdPartyData;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class ProcessBWorkflowImpl extends BaseProcessWorkflow implements ProcessBWorkflow {

    private static final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(1))
            .build();

    private final ProcessBActivities activities =
            Workflow.newActivityStub(ProcessBActivities.class, options);

    @Override
    protected int defineExpectedCount() {
        return 5;  // Process B needs 5 messages
    }

    @Override
    public void run(String requestId, int expectedCount) {
        ThirdPartyData tp = activities.fetchThirdParty(requestId);
        activities.publishInternalRequests(requestId);
        waitFor(expectedCount);
        activities.completeBusinessLogic(requestId, tp, this.internalDatas);
    }
}

