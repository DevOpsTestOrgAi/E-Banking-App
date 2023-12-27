package com.ebanking.TransferService.model;

public enum TransferState {
    TO_BE_SERVED,
    SERVED,
    EXTOURNED,
    RESET,
    BLOCKED,
    UNBLOCKED,
    ESCHEAT
}