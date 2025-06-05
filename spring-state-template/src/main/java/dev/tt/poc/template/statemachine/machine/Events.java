package dev.tt.poc.template.statemachine.machine;

public enum Events {
    BEGIN_PROCESS,           // entry into SERVICE_A_WAIT
    SERVICE_A_REPLY,         // A → B
    SERVICE_B_REPLY,         // B → C
    SERVICE_C_REPLY,         // C → D
    SERVICE_D_REPLY,         // D → E
    SERVICE_E_REPLY        // E → COMPLETE
}