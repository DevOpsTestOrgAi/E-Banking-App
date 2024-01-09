package com.ebanking.ClientService.service;

import com.ebanking.ClientService.entity.Beneficiary;
import com.ebanking.ClientService.entity.Customer;
import com.ebanking.ClientService.entity.TransferEntity;
import com.ebanking.ClientService.external.client.ExternalNotificationService;
import com.ebanking.ClientService.external.client.TransferClient;
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
        try {
            // Get the transfer by reference
            ResponseEntity<TransferEntity> transferResponse = transferClient.getTransferByReference(transferWithdrawRequest.getRef());

            // Check if the transfer response is not OK or the body is null, return a response with status 500
            if (transferResponse.getStatusCode() != HttpStatus.OK || transferResponse.getBody() == null) {
                return ServeTransferResponse.builder()
                        .message(" Référence de transfert invalide")
                        .isServed(false)
                        .build();
            }

            TransferEntity transfer = transferResponse.getBody();

            // Check if the transfer entity is null
            if (transfer == null) {
                return ServeTransferResponse.builder().message("Référence de transfert invalide").isServed(false).build();
            }

            // Check transfer state
            switch (transfer.getState()) {
                case SERVED -> {
                    return ServeTransferResponse.builder().message("Le retrait a déjà été effectué.").isServed(false).build();
                }
                case EXTOURNED -> {
                    return ServeTransferResponse.builder().message("Le transfert est extourné.").isServed(false).build();
                }
                case BLOCKED -> {
                    return ServeTransferResponse.builder().message("Le transfert est bloqué.").isServed(false).build();
                }
                default -> {
                    // Proceed with further processing if none of the above states
                }
            }

            // Process based on transfer type
            return processTransferType(transfer, transferWithdrawRequest);
        } catch (Exception e) {
            // Handle any exceptions that may occur
            return ServeTransferResponse.builder()
                    .message("Reference fourni est incorrect! ")
                    .isServed(false)
                    .build();
        }
    }

    private ServeTransferResponse processTransferType(TransferEntity transfer, TransferWithdrawRequest transferWithdrawRequest) {
        try {
            // Process based on transfer type
            if (transferWithdrawRequest.getTransferType() == TransferType.BANK_TO_GAB ||
                    transferWithdrawRequest.getTransferType() == TransferType.WALLET_TO_GAB) {

                // GAB Transfer logic
                return processGABTransfer(transfer, transferWithdrawRequest);
            } else if (transferWithdrawRequest.getTransferType() == TransferType.BANK_TO_BANK ||
                    transferWithdrawRequest.getTransferType() == TransferType.WALLET_TO_BANK) {

                // Bank Transfer logic
                return processBankTransfer(transfer, transferWithdrawRequest);
            } else {
                return ServeTransferResponse.builder().message("Type de transfert inconnu").isServed(false).build();
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during type-specific processing
            return ServeTransferResponse.builder()
                    .message("Reference fourni est incorrect! ")
                    .isServed(false)
                    .build();
        }
    }

    private ServeTransferResponse processGABTransfer(TransferEntity transfer, TransferWithdrawRequest transferWithdrawRequest) {
        try {
            // GAB Transfer logic
            if (transfer.getMaxPIN_Attempts() >= 7) {
                transferClient.blockTransfer(transfer.getId());
                return ServeTransferResponse.builder().message("Le transfert est bloqué en raison du dépassement du nombre maximum de tentatives de PIN").isServed(false).build();
            }

            if (Objects.equals(transferWithdrawRequest.getPin(), transfer.getPINCode()) && !transferWithdrawRequest.getPin().isEmpty()) {
                transferClient.serveTransfer(transfer.getId());
                return ServeTransferResponse.builder().transferID(transfer.getId()).message("Le retrait a été effectué avec succès").isServed(true).build();
            } else {
                int counter = transfer.getMaxPIN_Attempts() + 1;
                transfer.setMaxPIN_Attempts(counter);
                transferClient.updateMaxPINAttempts(transfer.getId(), counter);
                return ServeTransferResponse.builder().message("PIN incorrect !").isServed(false).build();
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during GAB transfer processing
            return ServeTransferResponse.builder()
                    .message("Reference fourni est incorrect! ")
                    .isServed(false)
                    .build();
        }
    }

    private ServeTransferResponse processBankTransfer(TransferEntity transfer, TransferWithdrawRequest transferWithdrawRequest) {
        try {
            // Bank Transfer logic
            Optional<Beneficiary> beneficiary = beneficiaryRepository.findByCinAndTransferID(
                    transferWithdrawRequest.getCin(),
                    transfer.getId()
            );

            if (beneficiary.isEmpty()) {
                return ServeTransferResponse.builder().message("Le numéro d'identification est incorrect !").isServed(false).build();
            }

            Beneficiary b = beneficiary.get();
            if (Objects.equals(transferWithdrawRequest.getCin(), b.getCin()) && transfer.getId() == b.getTransferID()) {
                transferClient.serveTransfer(transfer.getId());
                return ServeTransferResponse.builder().transferID(transfer.getId()).message("Le retrait a été effectué avec succès").isServed(true).build();
            }

            return ServeTransferResponse.builder().message("Les données fournies sont incorrectes").isServed(false).build();
        } catch (Exception e) {
            // Handle any exceptions that may occur during bank transfer processing
            return ServeTransferResponse.builder()
                    .message("La référence fournie est incorrecte !")
                    .isServed(false)
                    .build();
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
