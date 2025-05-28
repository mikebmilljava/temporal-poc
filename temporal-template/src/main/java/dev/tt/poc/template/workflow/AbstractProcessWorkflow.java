package dev.tt.poc.template.workflow;

import dev.tt.poc.template.domain.InternalData;

public interface AbstractProcessWorkflow {
    void run(String requestId);

    void signalInternalData(String requestId, InternalData data);

    void waitFor(int expectedCount);
}
