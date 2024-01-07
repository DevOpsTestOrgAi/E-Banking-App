package com.ebanking.TransferService.service;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.TransferEntity;
import com.ebanking.TransferService.model.RestitutionTransferResponse;
import com.ebanking.TransferService.model.TransferHistoriesResponse;
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

    //TODO : Add batch   blocking transfers-->
    //TODO : Handle logic to Block/unblock a transfer
    //TODO : Add batch   Restitution transfers : Refund money to wallet
    //TODO : Add batch   Cancel transfers   : refund money to wallet and make and change the status of  Transfer object stored in DB
    void unblockTransfer(Long transferId);

    // Restitution du transfer  national
 //    @Override
 //    public RestitutionTransferResponse restitutionTransfer(Long transferID  ){
 //            //
 //            Optional<TransferEntity> transfer =transferRepository.findById(transferID);
 //            if(transfer.isPresent()){
 //                List<Customer> customers=new ArrayList<>();
 //                TransferEntity tr=transfer.get();
 //                //should check if  the transfer is served
 //                if(tr.getType()==TransferType.WALLET_TO_WALLET&&tr.getState()==TransferState.SERVED){
 //                    List<Long> beneficiariesIds = tr.getIdsAsList();
 //                    List<Long> beneficiaryWalletsIds=tr.getWalletIds();
 //                    List<Double> amounts = tr.getAmounts();
 //
 //                 Wallet customerWallet=externalClientService.getWalletByWalletID(tr.getCustomerWalletId());
 //                 // here  beneficiariesIds are like 1|2|
 //                    // beneficiaryWalletsIds  are like 22|11|   means the beneficiary with id=1 has wallet with id 22 , the same about beneficiary with id=2 he has walletid =11
 //                    /*
 //                     each beneficiary has received an amount      like the beneficiary  with the id =1  has received the amount 100.00  the list  of amounts is looking like this :  100|
 //                     so i want to check if each  one of beneficairy has Balance of wallet > amount
 //                     */
 //                    for (int i = 0; i < beneficiariesIds.size(); i++) {
 //                        Long beneficiaryID  = beneficiariesIds.get(i);
 //                        Long beneficiaryWalletID =beneficiaryWalletsIds.get(i);
 //                        double amount =amounts.get(i);
 //                        if(externalClientService.getWalletByWalletID(beneficiaryWalletID).getBalance()>amount){
 //                            externalClientService.updateWalletBalance(tr.getCustomer().getId(),customerWallet.getBalance()+amount);
 //                            externalClientService.updateWalletBalance(beneficiaryID,externalClientService.getWalletByWalletID(beneficiaryWalletID).getBalance()-amount);
 //                           Optional<Customer>  c  = externalClientService.getCustomerById(beneficiaryID);
 //                            c.ifPresent(customers::add);
 //
 //                        }
 //
 //
 //                    } if(customers.isEmpty()){
 //                        //return the  response
 //                        return RestitutionTransferResponse.builder()
 //                                .customerList(null)
 //                                .isRestitutive(false)
 //                                .message("Error while reinstituting transfers : Customers Balance is instantaneous")
 //                                .build();
 //
 //                    }else {
 //
 //
 //                        //return the  response
 //                        return RestitutionTransferResponse.builder()
 //                                .customerList(customers)
 //                                .isRestitutive(true)
 //                                .message("Restitutive Transfers ")
 //
 //                                .build();
 //
 //                    }
 //
 //
 //                }if((tr.getType()==TransferType.WALLET_TO_BANK||
 //                        tr.getType()==TransferType.WALLET_TO_GAB)&&
 //                                        (tr.getState()==TransferState.TO_BE_SERVED||
 //                                                tr.getState()==TransferState.BLOCKED)){
 //                    Wallet customerWallet=externalClientService.getWalletByWalletID(tr.getCustomerWalletId());
 //                    externalClientService.updateWalletBalance(tr.getCustomer().getId(),customerWallet.getBalance()+tr.getAmount());
 //                     Optional<Customer> c =externalClientService.getCustomerById(tr.getWalletIds().get(0));
 //                    Customer customer = null;
 //
 //                     if(c.isPresent()){
 //                        customer =c.get();
 //                         customers.add(customer);
 //                     }
 //                    return RestitutionTransferResponse.builder()
 //                            .customerList(customers)
 //                            .message("Restitutive Transfers succesffly")
 //                            .build();
 //
 //
 //                }
 //                if((tr.getType()==TransferType.BANK_TO_BANK||
 //                        tr.getType()==TransferType.BANK_TO_GAB)&&
 //                        (tr.getState()==TransferState.TO_BE_SERVED||
 //                                tr.getState()==TransferState.BLOCKED)){
 //                    Wallet customerWallet=externalClientService.getWalletByWalletID(tr.getCustomerWalletId());
 //                    externalClientService.updateWalletBalance(tr.getCustomer().getId(),customerWallet.getBalance()+tr.getAmount());
 //                    Customer customer = null;
 //                    Optional<Customer> c =externalClientService.getCustomerById(tr.getWalletIds().get(0));
 //
 //                    if(c.isPresent()){
 //
 //                        customer =c.get();
 //                        customers.add(customer);
 //                    }
 //                    return RestitutionTransferResponse.builder()
 //                            .customerList(customers)
 //                            .message("Restitutive Transfers succesffly")
 //                            .build();
 //
 //
 //
 //
 //                }
 //                if(tr.getState()==TransferState.RESET)
 //                return RestitutionTransferResponse.builder()
 //                        .customerList(null)
 //                        .message("Transfer is already Restitued ")
 //                        .build();
 //
 //                }
 //
 //
 //
 //            }else return  RestitutionTransferResponse.builder()
 //                    .isRestitutive(false)
 //                    .customerList(null)
 //                    .message("transfer is not exist")
 //                    .build();
 //
 //
 //    }
    RestitutionTransferResponse restitutionTransfer(Long transferID);

    List<TransferHistoriesResponse> getTransferHistoriesByCustomerIdNumber(String idNumber);
}
