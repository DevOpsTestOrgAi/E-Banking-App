package com.ebanking.bankTrackingService.model;

public enum TransferState {
    TO_BE_SERVED,
    SERVED,
    EXTOURNED,
    RESET,
    BLOCKED,
    UNBLOCKED,
    ESCHEAT
}