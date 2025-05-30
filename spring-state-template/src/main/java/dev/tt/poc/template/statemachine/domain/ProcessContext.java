package dev.tt.poc.template.statemachine.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessContext {
    private String requestId;

    private int expectedSignalCount;

    private ThirdPartyData thirdPartyData;

    @Builder.Default
    private List<InternalData> receivedData = new ArrayList<>();

    public void addInternalData(InternalData data) {
        this.receivedData.add(data);
    }

    public int getReceivedCount() {
        return receivedData.size();
    }
}
