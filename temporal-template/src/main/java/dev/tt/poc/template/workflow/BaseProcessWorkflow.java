package dev.tt.poc.template.workflow;

import dev.tt.poc.template.activities.ProcessActivities;
import dev.tt.poc.template.domain.InternalData;
import dev.tt.poc.template.domain.ThirdPartyData;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseProcessWorkflow implements AbstractProcessWorkflow {
    // Activities common to all processes
    protected final ProcessActivities activities =
            Workflow.newActivityStub(ProcessActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(30))
                            .build());

    // Workflow state
    protected final List<InternalData> internalDatas = new ArrayList<>();
    private ThirdPartyData tpData;
    private int expectedCount;

    @Override
    public final void run(String requestId) {
        this.expectedCount = defineExpectedCount();
        // 1) Third-party call
        this.tpData = activities.fetchThirdParty(requestId);
        // 2) Publish internal requests
        activities.publishInternalRequests(requestId);
        // 3) Await signals
        Workflow.await(() -> internalDatas.size() >= expectedCount);
        // 4) Business logic
        activities.completeBusinessLogic(requestId, tpData, internalDatas);
    }

    @Override
    public final void signalInternalData(String requestId, InternalData data) {
        internalDatas.add(data);
    }

    @Override
    public void waitFor(int expectedCount) {
        // Temporal SDK’s way to block until our condition becomes true
        Workflow.await(() -> internalDatas.size() >= expectedCount);
    }

    protected abstract int defineExpectedCount();
}
