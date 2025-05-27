package dev.tt.poc.subscription.workflow;


import dev.tt.poc.subscription.model.EmailDetails;
import dev.tt.poc.subscription.model.WorkflowData;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

public abstract class BaseProcessWorkflow implements AbstractProcessWorkflow {
    // Activities common to all processes
    protected final ProcessActivities activities =
            Workflow.newActivityStub(ProcessActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(30))
                            .build());

    // Workflow state
    private final List<InternalData> internalDatas = new ArrayList<>();
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

    protected abstract int defineExpectedCount();
}
