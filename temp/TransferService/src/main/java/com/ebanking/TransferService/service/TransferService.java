package com.ebanking.TransferService.service;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.TransferEntity;
import com.ebanking.TransferService.model.TransferRequest;
import com.ebanking.TransferService.model.TransferResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TransferService {
//  /*  /**
//     * Initiates a new transfer based on the provided transfer request.
//     *
//     * @param transferRequest The details of the transfer to be initiated.
//     * @return The unique identifier for the initiated transfer.
//     */
//    String initiateTransfer(TransferRequest transferRequest);
//
//    /**
//     * Completes the transfer process when the beneficiary presents themselves.
//     *
//     * @param transferId The unique identifier of the transfer to be served.
//     */
//    void serveTransfer(String transferId);
//
//    /**
//     * Cancels a transfer that hasn't been served yet.
//     *
//     * @param transferId The unique identifier of the transfer to be canceled.
//     */
//    void cancelTransfer(String transferId);
//
//    /**
//     * Allows for the reversal of a transfer initiated by the agent in case of an error.
//     *
//     * @param transferId       The unique identifier of the transfer to be extourned.
//     * @param extourneReason   The reason for extourning the transfer.
//     */
//    void extourneTransfer(String transferId, String extourneReason);
//
//    /**
//     * Retrieves details about a specific transfer.
//     *
//     * @param transferId The unique identifier of the transfer.
//     * @return Details of the specified transfer.
//     */
//    TransferDetails getTransferDetails(String transferId);
//    /**
//     * Blocks a transfer to prevent it from being served.
//     *
//     * @param transferId The unique identifier of the transfer to be blocked.
//     * @param blockReason The reason for blocking the transfer.
//     */
//    void blockTransfer(String transferId, String blockReason);
//
//    /**
//     * Unblocks a previously blocked transfer to allow it to be served.
//     *
//     * @param transferId The unique identifier of the transfer to be unblocked.
//     */
//    void unblockTransfer(String transferId);
//    /*
//
TransferResponse initiateTransfer(TransferRequest transferRequest) throws IOException;
void serveTransfer( Long transferID);
Optional<TransferEntity> getTransferByRef(String ref );

void updateMaxPINAttempts(Long transferId, int newMaxPINAttempts);
void blockTransfer(Long transferId);


    byte[] getTransferReceipt(Long transferId);
}
