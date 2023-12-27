package com.ebanking.TransferService.service;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.Customer;
import com.ebanking.TransferService.entity.TransferEntity;
import com.ebanking.TransferService.entity.Wallet;
import com.ebanking.TransferService.external.client.ExternalClientService;
import com.ebanking.TransferService.external.client.ExternalNotificationService;
import com.ebanking.TransferService.model.*;
import com.ebanking.TransferService.repository.TransferRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Log4j2
@Transactional
@Service
    public class TransferServiceImpl implements TransferService {
         @Autowired
         private ExternalClientService externalClientService;
         @Autowired
         ExternalNotificationService externalNotificationService;
         @Autowired
         Producer producer ;
         @Autowired
         TransferReceipt transferReceipt;



        private final TransferRepository transferRepository;

        @Autowired
        public TransferServiceImpl(TransferRepository transferRepository) {
            this.transferRepository = transferRepository;
        }
    private static String generateReferenceNumber() {
        // Prefix for the reference number
        String prefix = "837";

        // Generate 10 random digits
        String randomDigits = generateRandomDigits();

        // Concatenate the prefix and random digits
        return prefix + randomDigits;
    }

    private static String generateRandomDigits() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int digit = (int) (Math.random() * 10);
            builder.append(digit);
        }

        return builder.toString();
    }
    public List<Beneficiary> getBeneficiariesByIds(List<Long> beneficiaryIds) {
        List<Beneficiary> beneficiaries = new ArrayList<>();

        for (Long id : beneficiaryIds) {
            beneficiaries.add(externalClientService.getBeneficiaryById(id));

        }

        return beneficiaries;
    }

        @Override
        @Transactional
        public TransferResponse initiateTransfer(TransferRequest transferRequest) throws JsonProcessingException {
            // Recherche du client par ID
            Optional<Customer> customerOptional = externalClientService.getCustomerById(transferRequest.getCustomerID());

            // Vérification de la présence du client
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                Wallet wallet = externalClientService.getWalletById(customer.getId());
                double totalAmount=getTotalAmountWithFees(transferRequest.getAmounts(),transferRequest.getFeeType());
                transferRequest.setAmount(totalAmount);
                // Calcul du montant avec les frais
                transferRequest.setAmount(calculateAmountWithFee(totalAmount, transferRequest.getFeeType()));
                List<Long> ids = transferRequest.getBeneficiaries_ids();
                List<Beneficiary> beneficiaries=getBeneficiariesByIds(transferRequest.getBeneficiaries_ids());
                // Vérification de l'OTP
                if (Objects.equals(transferRequest.getOtp(), customer.getOtp())) {
                    // Gestion du type de transfert
                    if (transferRequest.getType() == TransferType.WALLET_TO_WALLET) {
                        // Vérification du solde du portefeuille
                        if (wallet.getBalance() > transferRequest.getAmount()) {
                            // Création et sauvegarde de l'entité de transfert
                            transferRequest.setReference(generateReferenceNumber());
                            TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
//                            ResponseEntity<Void> updateWalletBalance(
//                                    @PathVariable Long customerID,
//                            @RequestParam double newBalance);
                          externalClientService.updateWalletBalance(transferRequest.getCustomerID(),wallet.getBalance() -transferRequest.getAmount());
                            // before saving the      transferEntity    beneficiaryAmount  from each beneficiary wallet
                            List<Long> beneficiaryIds = transferRequest.getBeneficiaries_ids();
                            List<Double> amounts = transferRequest.getAmounts();

                            for (int i = 0; i < beneficiaryIds.size(); i++) {
                                Long beneficiaryId = beneficiaryIds.get(i);
                                Double amount = amounts.get(i);

                                // Update the wallet balance for the giver (assuming id corresponds to the giver)
                                Wallet beneficiaryWallet = externalClientService.getWalletById(beneficiaryId);
                                externalClientService.updateWalletBalance(beneficiaryId,
                                        beneficiaryWallet.getBalance()+calculateAmountWithFee(amount,
                                                transferRequest.getFeeType()));//                              @PutMapping("/update-customer-to-customer-id/{customerID}")
//                            public ResponseEntity<Customer> updateCustomerToCustomerID(
//                            @PathVariable long customerID,
//                            @RequestParam long customerToCustomerID)
                               externalClientService.updateCustomerToCustomerID(beneficiaryId,transferRequest.getCustomerID());


                            }
                          transferRepository.save(transferEntity);
                          // filling the  transfer recipient  with data:
                            String orderingFullName=customer.getFirstName()+" "+customer.getLastName();
                             List<String> beneficiariesFullName = new ArrayList<>();
                             double amount=transferRequest.getAmount();
                             String orderingRib =customer.getRib();
                              List<String>  beneficiariesRibs= new ArrayList<>();
                              for(Long id  :  transferRequest.getBeneficiaries_ids()){
                                  beneficiariesFullName.add(externalClientService.getBeneficiaryById(id).getFirstName()+
                                          " "+externalClientService.getBeneficiaryById(id).getLastName());
                                  beneficiariesRibs.add(externalClientService.getBeneficiaryById(id).getRib());


                              }

                             String initiatedAt=transferEntity.getInitiatedAt();
                            // Call the TransferReceipt bean and fill it with data
                            transferReceipt.setOrderingFullName(orderingFullName);
                            transferReceipt.setBeneficiariesFullName(beneficiariesFullName);
                            transferReceipt.setAmount(amount);
                            transferReceipt.setOrderingRib(orderingRib);
                            transferReceipt.setBeneficiariesRibs(beneficiariesRibs);
                            transferReceipt.setInitiatedAt(initiatedAt);

                            String path  = ".0..................................................................................................................................................................................../";
                            // Generate the PDF
                            transferReceipt.generateTransferReceipt(path+transferEntity.getReference()+"-recipient");


                            producer.sendMessage(transferEntity);


                            // Réponse de succès
                            return TransferResponse.builder().message("transfert ajouté avec la référence : "
                                    + transferEntity.getReference()).isInitaited(true).build();
                        } else {
                            // Réponse en cas de solde insuffisant
                            return TransferResponse.builder().message("solde insuffisant! ")
                                    .isInitaited(false).build();
                        }
                    }
                    if (transferRequest.getType() == TransferType.WALLET_TO_GAB) {
                        // Configuration de la référence du transfert
                        transferRequest.setReference(generateReferenceNumber());
                        transferRequest.setFeeType(FeeType.FEE_BENEFICIARY);

                        // Calcul du montant avec les frais
                        if (transferRequest.getIsNotificationsSendingChosen()) {
                            transferRequest.setAmount(calculateAmountWithFee(transferRequest.getAmount() + 2.00,
                                    transferRequest.getFeeType()));

                            // Envoi du SMS
                            externalNotificationService.sendSMS(SMS.builder().
                                    ref(transferRequest.getReference()).
                                    pin(transferRequest.getPINCode()).
                                    beneficiaries(beneficiaries).customer(customer).
                                    amount(transferRequest.getAmount()).transferType(transferRequest.getType())
                                    .sendRef(true)
                                    .build());
                        } else {
                            transferRequest.setAmount(calculateAmountWithFee(transferRequest.getAmount(), transferRequest.getFeeType()));
                        }

                        // Création et sauvegarde de l'entité de transfert
                        TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
                        transferRepository.save(transferEntity);
                        producer.sendMessage(transferEntity);

                        // Réponse de succès
                        return TransferResponse.builder().message("transfert ajouté avec la référence : "
                                + transferEntity.getReference()).isInitaited(true).build();
                    } else if (transferRequest.getType() == TransferType.BANK_TO_GAB) {
                        // Configuration de la référence du transfert
                        transferRequest.setReference(generateReferenceNumber());

                        // Calcul du montant avec les frais
                        if (transferRequest.getIsNotificationsSendingChosen()) {
                            transferRequest.setAmount(calculateAmountWithFee(transferRequest.getAmount() + 2.00,
                                    transferRequest.getFeeType()));

                            // Envoi du SMS
                            externalNotificationService.sendSMS(SMS.builder().
                                    ref(transferRequest.getReference()).
                                    pin(transferRequest.getPINCode()).
                                    beneficiaries(beneficiaries).customer(customer).
                                    amount(transferRequest.getAmount()).transferType(transferRequest.getType())
                                    .sendRef(true)
                                    .build());
                        } else {
                            transferRequest.setAmount(calculateAmountWithFee(transferRequest.getAmount(),
                                    transferRequest.getFeeType()));
                        }

                        // Création et sauvegarde de l'entité de transfert
                        TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
                        TransferEntity transfer = transferRepository.save(transferEntity);
                        long id = transfer.getId();
                        System.out.println(id);
                       log.info("TransferIDd="+id);
                        for (Long beniID  : transferRequest.getBeneficiaries_ids()) {
                            log.info("beniID="+id);
                            externalClientService.updateTransferIDInBeneficiary(beniID, id);
                        }

                        //updateTransferIDInBeneficiary(
                        //            @PathVariable Long beneficiaryId,
                        //            @RequestParam Long transferId)
                        producer.sendMessage(transferEntity);


                        // Réponse de succès
                        return TransferResponse.builder().message("transfert ajouté avec la référence :"
                                + transferEntity.getReference()).isInitaited(true).build();
                    }
 //                   else if (transferRequest.getType() == TransferType.BANK_TO_BANK) {
//                        // Configuration de la référence du transfert
//                        transferRequest.setReference(generateReferenceNumber());
//
//                        // Calcul du montant avec les frais
//                        if (transferRequest.getIsNotificationsSendingChosen()) {
//                            transferRequest.setAmount(calculateAmountWithFee(transferRequest.getAmount() + 2.00, transferRequest.getFeeType()));
//
//                            // Envoi du SMS
//                            externalNotificationService.sendSMS(SMS.builder().
//                                    ref(transferRequest.getReference()).
//                                    pin(transferRequest.getPINCode()).
//                                    beneficiaries(transferRequest.getBeneficiaries()).
//                                    customer(customer).amount(transferRequest.getAmount()).build());
//                        } else {
//                            transferRequest.setAmount(calculateAmountWithFee(transferRequest.getAmount(),
//                                                                                                transferRequest.getFeeType()));
//                        }
//
//                        // Création et sauvegarde de l'entité de transfert
//                        TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
//                        transferRepository.save(transferEntity);
//                        producer.sendMessage(transferEntity);
//
//                        // Réponse de succès
//                        return TransferResponse.builder().message("transfert ajouté avec la référence : " +
//                                transferEntity.getReference()).isInitaited(true).build();
//                    } else {
//                        // Type de transfert non supporté
//                        return TransferResponse.builder().message("Type de transfert non supporté").
//                                isInitaited(false).build();
//                    }

                    //DEBUGING
                    return TransferResponse.builder().message("OTP good").
                            isInitaited(false).build();
                } else {
                    // Cas où l'OTP est incorrect
                    return TransferResponse.builder().message("OTP incorrect").
                            isInitaited(false).build();
                }
            } else {
                // Cas où le client n'est pas trouvé
                return TransferResponse.builder().message("Client introuvable").
                        isInitaited(false).build();
            }
        }
    public double getTotalAmountWithFees(List<Double> amounts, FeeType feeType) {
        return amounts.stream()
                .mapToDouble(amount -> calculateAmountWithFee(amount, feeType))
                .sum();
    }
    private double calculateAmountWithFee(double transactionAmount, FeeType feeType) {
        // Assuming a fixed fee for illustration purposes
        double fixedFee;
        if (transactionAmount < 1000.00) {
            fixedFee = 20.00;
        } else if (transactionAmount < 10000.00) {
            fixedFee = 50.00;
        } else if (transactionAmount < 20000.00) {
            fixedFee = 100.00;
        } else {
            // Default case, you can set it to 150.00 or any other value as needed
            fixedFee = 150.00;
        }  // Modify this value based on your business rules


        return switch (feeType) {
            case FEE_CLIENT_ORDERING ->
                // Frais à la charge du client d’donneur d’ordre
                    transactionAmount + fixedFee;
            case FEE_BENEFICIARY ->
                // Frais à la charge du client bénéficiaire
                // No fee for the beneficiary
                    transactionAmount;
            case FEE_SHARED ->
                // Frais partagés entre les clients (Donneur d’ordre et bénéficiaire)
                    transactionAmount + 0.5 * fixedFee;
            default ->
                // Handle unsupported fee type or set default values
                    transactionAmount;
        };
    }

    private TransferEntity buildTransferEntity(TransferRequest transferRequest,  Customer customer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return TransferEntity.builder()
                .reference(transferRequest.getReference())
                .amount(transferRequest.getAmount())
                .maxBAmountPeriodC(transferRequest.getMaxBAmountPeriodC())
                .maxTransfersPerCustomer(transferRequest.getMaxTransfersPerCustomer())
                .toBeServedFrom(transferRequest.getToBeServedFrom())
                .PINCode(transferRequest.getPINCode())
                .validationDuration(transferRequest.getValidationDuration())
                .customer(customer)
                .initiatedAt(LocalDateTime.now().format(formatter))
               // .beneficiaries(getBeneficiariesByIds(transferRequest.getBeneficiaries_ids()))
                .state(transferRequest.getState())
                .type(transferRequest.getType())
                .build();
    }

    @Override
    public void serveTransfer(Long transferID) {
        Optional<TransferEntity> optionalTransfer = transferRepository.findById(transferID);
        TransferEntity transferEntity = optionalTransfer.get();

        // Update the transfer state to "SERVED"
        transferEntity.setState(TransferState.SERVED);

        // Save the updated transfer entity
        transferRepository.save(transferEntity);


    }

    @Override
    public Optional<TransferEntity> getTransferByRef(String ref) {
        return transferRepository.findByReference(ref) ;
    }

    @Override
    public void updateMaxPINAttempts(Long transferId, int newMaxPINAttempts) {
        TransferEntity transfer = transferRepository.findById(transferId).orElse(null);
        if (transfer != null) {
            transfer.setMaxPIN_Attempts(newMaxPINAttempts);
            transferRepository.save(transfer);

        }

        }
    @Override
    public void blockTransfer(Long transferId) {
        TransferEntity transfer = transferRepository.findById(transferId).orElse(null);
        if (transfer != null) {
            transfer.setState(TransferState.BLOCKED);
            transferRepository.save(transfer);
        }
    }

}



