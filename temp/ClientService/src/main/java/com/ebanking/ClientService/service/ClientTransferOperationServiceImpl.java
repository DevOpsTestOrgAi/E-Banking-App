package com.ebanking.ClientService.service;

import com.ebanking.ClientService.entity.Beneficiary;
import com.ebanking.ClientService.entity.TransferEntity;
import com.ebanking.ClientService.entity.Wallet;
import com.ebanking.ClientService.external.client.TransferClient;
import com.ebanking.ClientService.model.TransferState;
import com.ebanking.ClientService.model.TransferWithdrawRequest;
import com.ebanking.ClientService.model.ServeTransferResponse;
import com.ebanking.ClientService.model.TransferType;
import com.ebanking.ClientService.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientTransferOperationServiceImpl implements  ClientTransferOperationService{
    @Autowired
    TransferClient transferClient;
    @Autowired
    WalletRepository walletRepository;
    @Override
    public void markAsToServe(TransferEntity transfer) {

    }

    @Override
    public ServeTransferResponse markAsServed(TransferWithdrawRequest transferWithdrawRequest) {
        ResponseEntity<TransferEntity> transferResponse = transferClient.getTransferByReference(transferWithdrawRequest.getRef());

        if (transferResponse.getStatusCode() == HttpStatus.OK) {
            TransferEntity transfer = transferResponse.getBody();

            if (transfer != null) {
                // Check if the transfer has already been served or is in an invalid state
                if (transfer.getState() == TransferState.SERVED || transfer.getState() == TransferState.EXTOURNED || transfer.getState() == TransferState.BLOCKED) {
                    return ServeTransferResponse.builder().message("Transfer has already been served or is in an invalid state").isServed(false).build();
                }

                List<Beneficiary> beneficiaries = transferClient.getBeneficiariesByTransferId(transfer.getId());

                if (transferWithdrawRequest.getTransferType() == TransferType.BANK_TO_GAB
                        || transferWithdrawRequest.getTransferType() == TransferType.WALLET_TO_GAB) {

                    // Check if transfer is blocked
                    if (transfer.getMaxPIN_Attempts() >= 5) {
                        transferClient.blockTransfer(transfer.getId());
                        return ServeTransferResponse.builder().message("Transfer is blocked due to exceeding maximum PIN attempts").isServed(false).build();
                    }

                    if (Objects.equals(transfer.getPINCode(), transferWithdrawRequest.getPin())) {
//
//                        if (transferWithdrawRequest.getTransferType() == TransferType.WALLET_TO_GAB) {
//
//                            double deductedAmount = transfer.getAmount();
//
//                            // Assuming a transfer has one wallet (update based on your specific scenario)
//                            Wallet wallet = transfer.getWallet();
//
//                            // Deduct the amount from the wallet balance
//                            double newBalance = wallet.getBalance() - deductedAmount;
//                            wallet.setBalance(newBalance);
//
//                            // Update the wallet in the database
//                            walletRepository.save(wallet);
//                        }



                        // Mark the transfer as served
                        transferClient.serveTransfer(transfer.getId());
                        return ServeTransferResponse.builder().message("Transfer is served Successfully").isServed(true).build();
                    } else {
                        // Increment the PIN attempts counter
                        int counter = transfer.getMaxPIN_Attempts() + 1;
                        transfer.setMaxPIN_Attempts(counter);
                        transferClient.updateMaxPINAttempts(transfer.getId(), counter);
                        return ServeTransferResponse.builder().message("Incorrect PIN!").isServed(false).build();
                    }

                } else if (transferWithdrawRequest.getTransferType() == TransferType.WALLET_TO_BANK
                        || transferWithdrawRequest.getTransferType() == TransferType.BANK_TO_BANK) {

                    Optional<Beneficiary> selectedBeneficiary = beneficiaries.stream()
                            .filter(beneficiary -> transferWithdrawRequest.getCin().equals(beneficiary.getCin()))
                            .findFirst();

                    if (selectedBeneficiary.isPresent()) {
                        // Deduct the amount from the customer's wallet balance
                        double deductedAmount = transfer.getAmount();
                        Wallet customerWallet = transfer.getCustomer().getWallets().get(0); // Assuming a customer has one wallet
                        customerWallet.setBalance(customerWallet.getBalance() - deductedAmount);
                        // Update the wallet in the database
                        walletRepository.save(customerWallet);

                        // Mark the transfer as served
                        transferClient.serveTransfer(transfer.getId());
                        return ServeTransferResponse.builder().message("Transfer is served Successfully").isServed(true).build();
                    } else {
                        return ServeTransferResponse.builder().message("CIN is Invalid!").isServed(false).build();
                    }

                } else {
                    // Handle unexpected transfer type if necessary
                    return ServeTransferResponse.builder().message("Invalid Transfer Type").isServed(false).build();
                }
            } else {
                return ServeTransferResponse.builder().message("Invalid Transfer ID").isServed(false).build();
            }
        } else {
            return ServeTransferResponse.builder().message("Reference is incorrect, try another").isServed(false).build();
        }
    }



    @Override
    public void cancelTransfer(TransferEntity transfer) {

    }

    @Override
    public void returnTransfer(TransferEntity transfer) {

    }

    @Override
    public void blockTransfer(TransferEntity transfer) {

    }

    @Override
    public void unblockTransfer(TransferEntity transfer) {

    }

    @Override
    public void markAsUnclaimed(TransferEntity transfer) {

    }
}
