package dev.tt.poc.template.statemachine.machine;

public enum States {
    START,
    FETCH_THIRD_PARTY,
    PUBLISH_INTERNAL,
    WAIT_FOR_INTERNAL,
    TIMEOUT,
    COMPLETE,
    END
}