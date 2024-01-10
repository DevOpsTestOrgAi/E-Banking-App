package com.ebanking.ClientService.service;

import com.ebanking.ClientService.entity.Beneficiary;
import com.ebanking.ClientService.entity.Customer;
import com.ebanking.ClientService.entity.TransferEntity;
import com.ebanking.ClientService.entity.Wallet;
import com.ebanking.ClientService.external.client.ExternalNotificationService;
import com.ebanking.ClientService.external.client.TransferClient;
import com.ebanking.ClientService.model.TransferState;
import com.ebanking.ClientService.model.TransferWithdrawRequest;
import com.ebanking.ClientService.model.ServeTransferResponse;
import com.ebanking.ClientService.model.TransferType;
import com.ebanking.ClientService.repository.BeneficiaryRepository;
import com.ebanking.ClientService.repository.CustomerRepository;
import com.ebanking.ClientService.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class ClientTransferOperationServiceImpl implements  ClientTransferOperationService{

    @Autowired
    TransferClient transferClient;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ExternalNotificationService externalNotificationService;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    public static String generate4DigitNumber() {
        // Generate a random 4-digit number
        Random random = new Random();
        int randomValue = 1000 + random.nextInt(9000);

        // Convert the number to a string and return
        return String.valueOf(randomValue);
    }
    @Override
    public void sendOTP(Long customerID) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerID);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            // Generate OTP
            String otp = generate4DigitNumber();
            customer.setOtp(otp);

            // Send OTP through external notification service
            externalNotificationService.verifyIdentity(customer.getPhone(), otp);

            // Save the updated customer entity with OTP
            customerRepository.save(customer);
        } else {

            System.out.println("Customer not found for ID: " + customerID);
        }
    }
    @Override
    public void markAsToServe(TransferEntity transfer) {

    }
    @Override
    public ServeTransferResponse markAsServed(TransferWithdrawRequest transferWithdrawRequest) {
        // Get the transfer by reference
        ResponseEntity<TransferEntity> transferResponse = transferClient.getTransferByReference(transferWithdrawRequest.getRef());
        if (transferResponse.getStatusCode() != HttpStatus.OK) {
            return ServeTransferResponse.builder().message("Reference is incorrect, try another").isServed(false).build();
        }

        TransferEntity transfer = transferResponse.getBody();
        if (transfer == null) {
            return ServeTransferResponse.builder().message("Invalid Transfer reference").isServed(false).build();
        }


        // Check transfer state
        switch (transfer.getState()) {
            case SERVED -> {
                return ServeTransferResponse.builder().message("Transfer is already served.").isServed(false).build();
            }
            case EXTOURNED -> {
                return ServeTransferResponse.builder().message("Transfer is extourned.").isServed(false).build();
            }
            case BLOCKED -> {
                return ServeTransferResponse.builder().message("Transfer is blocked.").isServed(false).build();
            }
            default -> {
            }
            // Proceed with further processing if none of the above states
        }

        // Process based on transfer type
        if (transferWithdrawRequest.getTransferType() == TransferType.BANK_TO_GAB ||
                transferWithdrawRequest.getTransferType() == TransferType.WALLET_TO_GAB) {

            // GAB Transfer logic
            if (transfer.getMaxPIN_Attempts() >= 7) {
                transferClient.blockTransfer(transfer.getId());
                return ServeTransferResponse.builder().message("Transfer is blocked due to exceeding maximum PIN attempts").isServed(false).build();
            }

            if (Objects.equals(transferWithdrawRequest.getPin(), transfer.getPINCode())&&!transferWithdrawRequest.getPin().isEmpty()) {
                transferClient.serveTransfer(transfer.getId());
                return ServeTransferResponse.builder().transferID(transfer.getId()).message("Transfer is served successfully").isServed(true).build();
            } else {
                int counter = transfer.getMaxPIN_Attempts() + 1;
                transfer.setMaxPIN_Attempts(counter);
                transferClient.updateMaxPINAttempts(transfer.getId(), counter);
                return ServeTransferResponse.builder().message("Incorrect PIN!").isServed(false).build();
            }

        }
        else if (transferWithdrawRequest.getTransferType() == TransferType.BANK_TO_BANK ||
                transferWithdrawRequest.getTransferType() == TransferType.WALLET_TO_BANK) {

            // Bank Transfer logic
            Optional<Beneficiary> beneficiary = beneficiaryRepository.findByCinAndTransferID(
                    transferWithdrawRequest.getCin(),
                    transfer.getId()
            );
            if (beneficiary.isEmpty()) {
                return ServeTransferResponse.builder().message("ID number is incorrect!").isServed(false).build();
            }

            Beneficiary b = beneficiary.get();
            if (Objects.equals(transferWithdrawRequest.getCin(), b.getCin()) && transfer.getId() == b.getTransferID()) {
                transferClient.serveTransfer(transfer.getId());
                return ServeTransferResponse.builder().transferID(transfer.getId()).message("Transfer is served successfully").isServed(true).build();
            }

            return ServeTransferResponse.builder().message("Provided data is incorrect").isServed(false).build();

        } else {
            return ServeTransferResponse.builder().message("Unknown transfer type").isServed(false).build();
        }
    }






//TODO-->  changeLOgs: Bank to Gab :  wallet to gab    : wallet to bank ,  bank to bank  : sending to benficiaries :  unknown :  data will be persisted in  Benefiary enity from it you can check for bank    Gab no need to check

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
