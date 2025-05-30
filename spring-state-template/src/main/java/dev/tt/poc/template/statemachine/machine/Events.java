package dev.tt.poc.template.statemachine.machine;

public enum Events {
    THIRD_PARTY_FETCHED,
    INTERNAL_SIGNAL,
    INTERNAL_COMPLETE,  // when count reached
    BUSINESS_COMPLETE,
    INTERNAL_TIMEOUT
}