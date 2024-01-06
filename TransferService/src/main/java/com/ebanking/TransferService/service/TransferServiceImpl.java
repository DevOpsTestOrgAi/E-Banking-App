package com.ebanking.TransferService.service;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.Customer;
import com.ebanking.TransferService.entity.TransferEntity;
import com.ebanking.TransferService.entity.Wallet;
import com.ebanking.TransferService.external.client.ExternalClientService;
import com.ebanking.TransferService.external.client.ExternalNotificationService;
import com.ebanking.TransferService.model.*;
import com.ebanking.TransferService.repository.TransferRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
         private ExternalNotificationService externalNotificationService;
//         @Autowired
//         private Producer producer ;
         @Autowired
         private  TransferReceipt transferReceipt;
         @Autowired
         private TransferReceiptWalletToGAB transferReceiptWalletToGAB;
         @Autowired
         private  TransferReceiptBankToGAB transferReceiptBankToGAB;
         @Autowired
         private TransferReceiptWalletToBANK transferReceiptWalletToBANK;
         @Autowired
         private TransferReceiptBANKToBANK  transferReceiptBANKToBANK;



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
    public TransferResponse initiateTransfer(TransferRequest transferRequest) throws IOException {
        // Recherche du client par ID
        Optional<Customer> customerOptional = externalClientService.getCustomerById(transferRequest.getCustomerID());

        // Vérification de la présence du client
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            Wallet wallet = externalClientService.getWalletById(customer.getId());
            double totalAmount = getTotalAmountWithFees(transferRequest.getAmounts(), transferRequest.getFeeType());
            transferRequest.setAmount(totalAmount);

            // Vérification de l'OTP
            if (Objects.equals(transferRequest.getOtp(), customer.getOtp())) {

                //-------------------------------------------WALLET_TO_WALLET
                // Gestion du type de transfert
                if (transferRequest.getType() == TransferType.WALLET_TO_WALLET) {
                    // Vérification du solde du portefeuille
                    if (wallet.getBalance() > transferRequest.getAmount()) {
                        List<String> beneficiariesFullName = new ArrayList<>();
                        List<String> beneficiariesRibs = new ArrayList<>();

                        // Création et sauvegarde de l'entité de transfert
                        transferRequest.setReference(generateReferenceNumber());
                        TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
                        externalClientService.updateWalletBalance(transferRequest.getCustomerID(), wallet.getBalance() - transferRequest.getAmount());
                        // before saving the transferEntity beneficiaryAmount from each beneficiary wallet
                        List<Long> beneficiaryIds = transferRequest.getBeneficiaries_ids();
                        List<Long> walletIds=new ArrayList<>();

                        List<Double> amounts = transferRequest.getAmounts();

                        for (int i = 0; i < beneficiaryIds.size(); i++) {
                            Long beneficiaryId = beneficiaryIds.get(i);
                            Optional<Customer> beneficiary = externalClientService.getCustomerById(beneficiaryId);
                            if (beneficiary.isPresent()) {
                                beneficiariesFullName.add(beneficiary.get().getFirstName() + " " + beneficiary.get().getLastName());
                                beneficiariesRibs.add(beneficiary.get().getRib());
                            }
                            Double amount = amounts.get(i);
                            // Update the wallet balance for the giver (assuming id corresponds to the giver)
                            Wallet beneficiaryWallet = externalClientService.getWalletById(beneficiaryId);
                            walletIds.add(beneficiaryWallet.getId());
                            externalClientService.updateWalletBalance(beneficiaryId,
                                    beneficiaryWallet.getBalance() + calculateAmountWithFee(amount, transferRequest.getFeeType()));

                            transferEntity.setCustomerWalletId(wallet.getId());


                            externalClientService.updateCustomerToCustomerID(beneficiaryId, transferRequest.getCustomerID());
                        }
                        transferEntity.addIds(transferRequest.getBeneficiaries_ids());
                        transferEntity.addWalletIds(walletIds);
                        // filling the transfer recipient with data:
                        String orderingFullName = customer.getFirstName() + " " + customer.getLastName();
                        double amount = transferRequest.getAmount();
                        String orderingRib = customer.getRib();
                        String initiatedAt = transferEntity.getInitiatedAt();
                        // Call the TransferReceipt bean and fill it with data
                        transferReceipt.setOrderingFullName(orderingFullName);
                        transferReceipt.setBeneficiariesFullName(beneficiariesFullName);
                        transferReceipt.setAmount(amount);
                        transferReceipt.setOrderingRib(orderingRib);
                        transferReceipt.setBeneficiariesRibs(beneficiariesRibs);
                        transferReceipt.setInitiatedAt(initiatedAt);
                        transferReceipt.setTransferType("Portefeuille à portefeuille");
                        transferReceipt.setReference(transferEntity.getReference());
                        transferReceipt.setIsInitiatedFromMobile(transferRequest.getIsInitiatedFromMobile());

                        // Generate the PDF content
                        String newPdfFileName = transferEntity.getReference() + "-recipient.pdf";
                        String newPdfPath = "G:/Microservices-ebanking-app/TransferService/" + newPdfFileName;
                        transferReceipt.generateTransferReceipt(newPdfPath);

                        // Read the content of the new PDF into a byte array
                        byte[] pdfContent = Files.readAllBytes(Paths.get(newPdfPath));

                        // Set the pdfContent property of transferEntity
                        transferEntity.setPdfContent(pdfContent);
                       //setting wallets  IDs  in transferEntity

                        // Set the PDF content to the TransferEntity
                        // saving the transfer object
                        transferEntity.AddAmounts(transferRequest.getAmounts());
                        TransferEntity te = transferRepository.save(transferEntity);

                        // Réponse de succès
                        return TransferResponse.builder().message("transfert ajouté avec la référence : " + te.getReference())
                                .transferID(te.getId()).ref(te.getReference()).isInitaited(true).build();
                    } else {
                        // Réponse en cas de solde insuffisant
                        return TransferResponse.builder().message("solde insuffisant! ").isInitaited(false).build();
                    }
                }
                //-----------------------------------------TRANSFERT DE PORTEFEUILLE À GAB
                if (transferRequest.getType() == TransferType.WALLET_TO_GAB) {
                    // Configuration de la référence du transfert
                    transferRequest.setReference(generateReferenceNumber());

                    if (wallet.getBalance() > transferRequest.getAmount()) {
                        // Réduction du montant total du portefeuille du client

                        // Calcul du montant avec les frais
                        if (transferRequest.getIsNotificationsSendingChosen()) {
                            // Ajout de frais pour l'envoi de notifications
                            transferRequest.setAmount(transferRequest.getAmount() + 2.00);

                            // Envoi du SMS
                            externalNotificationService.sendSMS(SMS.builder()
                                    .ref(transferRequest.getReference())
                                    .pin(transferRequest.getPin())
                                    .amount(transferRequest.getAmounts().get(0))
                                    .phone(transferRequest.getBeneficiaryRequest().getPhone())
                                    .customerFirstName(customer.getFirstName())
                                    .beneficiaryLastName(transferRequest.getBeneficiaryRequest().getLastName())
                                    .beneficiaryFirstName(transferRequest.getBeneficiaryRequest().getFirstName())
                                    .customerLastName(customer.getLastName())
                                    .transferType(transferRequest.getType())
                                    .sendRef(true)
                                    .build());

                        } else {
                            // Calcul du montant en incluant les frais selon le type de frais
                            transferRequest.setAmount(calculateAmountWithFee(transferRequest.getAmount(),
                                    transferRequest.getFeeType()));
                        }

                        // Mise à jour du solde du portefeuille
                        externalClientService.updateWalletBalance(transferRequest.getCustomerID(),
                                wallet.getBalance() - transferRequest.getAmount());

                        // Création et enregistrement de l'entité de transfert
                        TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
                        transferEntity.setCustomerWalletId(wallet.getId());
                        transferRequest.getBeneficiaryRequest().setCustomerID(transferRequest.getCustomerID());

                        // Préparation des informations pour le reçu de transfert
                        String beneficiaryFullName = transferRequest.getBeneficiaryRequest().getFirstName() + " "
                                + transferRequest.getBeneficiaryRequest().getLastName();
                        String orderingFullName = customer.getFirstName() + " " + customer.getLastName();
                        double amount = transferRequest.getAmount();
                        String orderingRib = customer.getRib();
                        String initiatedAt = transferEntity.getInitiatedAt();

                        // Remplissage du reçu de transfert avec les données
                        transferReceiptWalletToGAB.setOrderingFullName(orderingFullName);
                        transferReceiptWalletToGAB.setBeneficiaryFullName(beneficiaryFullName);
                        transferReceiptWalletToGAB.setAmount(amount);
                        transferReceiptWalletToGAB.setOrderingRib(orderingRib);
                        transferReceiptWalletToGAB.setInitiatedAt(initiatedAt);
                        transferReceiptWalletToGAB.setTransferType("Portefeuille à GAB");
                        transferReceiptWalletToGAB.setReference(transferEntity.getReference());
                        transferReceiptWalletToGAB.setIsInitiatedFromMobile(transferRequest.getIsInitiatedFromMobile());

                        // Génération du contenu PDF du reçu
                        String newPdfFileName = transferEntity.getReference() + "-recipient.pdf";
                        String newPdfPath = "G:/Microservices-ebanking-app/TransferService/" + newPdfFileName;
                        transferReceiptWalletToGAB.generateTransferReceipt(newPdfPath);

                        // Lecture du contenu du nouveau PDF dans un tableau de bytes
                        byte[] pdfContent = Files.readAllBytes(Paths.get(newPdfPath));

                        // Définition du contenu PDF dans l'entité de transfert
                        transferEntity.setPdfContent(pdfContent);

                        // Enregistrement de l'entité de transfert
                        TransferEntity te = transferRepository.save(transferEntity);

                        // Ajout du bénéficiaire à l'entité de transfert
                        BeneficiaryRequest beneficiaryRequest = BeneficiaryRequest.builder()
                                .firstName(transferRequest.getBeneficiaryRequest().getFirstName())
                                .lastName(transferRequest.getBeneficiaryRequest().getLastName())
                                .phone(transferRequest.getBeneficiaryRequest().getPhone())
                                .customerID(transferRequest.getCustomerID())
                                .transferID(te.getId())
                                .cin(transferRequest.getBeneficiaryRequest().getCin())
                                .build();
                        externalClientService.addBeneficiary(beneficiaryRequest);

                        // Envoi du message (si nécessaire)
                        // producer.sendMessage(transferEntity);

                        // Réponse de succès
                        return TransferResponse.builder()
                                .message("Transfert ajouté avec la référence : " + transferEntity.getReference())
                                .transferID(te.getId())
                                .ref(te.getReference())
                                .isInitaited(true)
                                .build();
                    } else {
                        // Réponse en cas de solde insuffisant
                        return TransferResponse.builder()
                                .message("Solde insuffisant!")
                                .isInitaited(false)
                                .build();
                    }
                }

                //-----------------------------------------WALLET_TO_BANK
                if (transferRequest.getType() == TransferType.WALLET_TO_BANK) {
                    // Configuration de la référence du transfert
                    transferRequest.setReference(generateReferenceNumber());

                    if (wallet.getBalance() > transferRequest.getAmount()) {
                        // Réduction du montant total du portefeuille du client
                        // Calcul du montant avec les frais
                        if (transferRequest.getIsNotificationsSendingChosen()) {
                            transferRequest.setAmount(transferRequest.getAmount() + 2.00);
                            // Envoi du SMS
                            externalNotificationService.sendSMS(SMS.builder()
                                    .ref(transferRequest.getReference())
                                    .amount(transferRequest.getAmounts().get(0))
                                    .phone(transferRequest.getBeneficiaryRequest().getPhone())
                                    .customerFirstName(customer.getFirstName())
                                    .beneficiaryLastName(transferRequest.getBeneficiaryRequest().getLastName())
                                    .beneficiaryFirstName(transferRequest.getBeneficiaryRequest().getFirstName())
                                    .customerLastName(customer.getLastName())
                                    .transferType(transferRequest.getType())
                                    .sendRef(true)
                                    .build());
                            externalClientService.updateWalletBalance(transferRequest.getCustomerID(), wallet.getBalance() - transferRequest.getAmount());
                        } else {
                            transferRequest.setAmount(calculateAmountWithFee(transferRequest.getAmount(), transferRequest.getFeeType()));
                            externalClientService.updateWalletBalance(transferRequest.getCustomerID(), wallet.getBalance() - transferRequest.getAmount());
                        }
                        // Création et sauvegarde de l'entité de transfert
                        TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
                        transferEntity.setCustomerWalletId(wallet.getId());
                        transferRequest.getBeneficiaryRequest().setCustomerID(transferRequest.getCustomerID());

                        // Préparation des informations pour le reçu de transfert
                        String beneficiaryFullName = transferRequest.getBeneficiaryRequest().getFirstName() + " " + transferRequest.getBeneficiaryRequest().getLastName();
                        String orderingFullName = customer.getFirstName() + " " + customer.getLastName();
                        double amount = transferRequest.getAmount();
                        String orderingRib = customer.getRib();
                        String initiatedAt = transferEntity.getInitiatedAt();

                        // Remplissage des données dans le reçu de transfert
                        transferReceiptWalletToBANK.setOrderingFullName(orderingFullName);
                        transferReceiptWalletToBANK.setBeneficiaryFullName(beneficiaryFullName);
                        transferReceiptWalletToBANK.setAmount(amount);
                        transferReceiptWalletToBANK.setOrderingRib(orderingRib);
                        transferReceiptWalletToBANK.setInitiatedAt(initiatedAt);
                        transferReceiptWalletToBANK.setCin(transferRequest.getBeneficiaryRequest().getCin());
                        transferReceiptWalletToBANK.setTransferType("Portefeuille à Banque");
                        transferReceiptWalletToBANK.setReference(transferEntity.getReference());
                        transferReceiptWalletToBANK.setIsInitiatedFromMobile(transferRequest.getIsInitiatedFromMobile());

                        // Génération du contenu PDF du reçu
                        String newPdfFileName = transferEntity.getReference() + "-recipient.pdf";
                        String newPdfPath = "G:/Microservices-ebanking-app/TransferService/" + newPdfFileName;
                        transferReceiptWalletToBANK.generateTransferReceipt(newPdfPath);

                        // Lecture du contenu du PDF dans un tableau de bytes
                        byte[] pdfContent = Files.readAllBytes(Paths.get(newPdfPath));
                        // Affectation du contenu PDF à l'entité de transfert
                        transferEntity.setPdfContent(pdfContent);

                        // Sauvegarde de l'entité de transfert
                        transferEntity.setPINCode(transferRequest.getPin());
                        TransferEntity te = transferRepository.save(transferEntity);

                        // Ajout du bénéficiaire à l'entité de transfert
                        BeneficiaryRequest beneficiaryRequest = BeneficiaryRequest.builder()
                                .firstName(transferRequest.getBeneficiaryRequest().getFirstName())
                                .lastName(transferRequest.getBeneficiaryRequest().getLastName())
                                .phone(transferRequest.getBeneficiaryRequest().getPhone())
                                .customerID(transferRequest.getCustomerID())
                                .transferID(te.getId())
                                .cin(transferRequest.getBeneficiaryRequest().getCin())
                                .build();
                        externalClientService.addBeneficiary(beneficiaryRequest);

                        // Réponse de succès
                        return TransferResponse.builder().message("transfert ajouté avec la référence : " +
                                transferEntity.getReference()).transferID(te.getId()).ref(te.getReference()).isInitaited(true).build();
                    } else {
                        // Réponse en cas de solde insuffisant
                        return TransferResponse.builder().message("solde insuffisant! ").isInitaited(false).build();
                    }
                }

//-------------------------------------------BANK_TO_GAB   // Cash Payment: You should have a Kyc data // servire un transfer en espece      dpeuis le console agent/back office
                    else if (transferRequest.getType() == TransferType.BANK_TO_GAB) {
                        transferRequest.setReference(generateReferenceNumber());
                        // Calcul du montant avec les frais
                        if (transferRequest.getIsNotificationsSendingChosen()) {
                            transferRequest.setAmount(transferRequest.getAmount() + 2.00);
                            // Envoi du SMS
                            externalNotificationService.sendSMS(SMS.builder()
                                    .ref(transferRequest.getReference())
                                    .amount(transferRequest.getAmounts().get(0))
                                    .phone(transferRequest.getBeneficiaryRequest().getPhone())
                                    .customerFirstName(customer.getFirstName())
                                    .beneficiaryLastName(transferRequest.getBeneficiaryRequest().getLastName())
                                    .beneficiaryFirstName(transferRequest.getBeneficiaryRequest().getFirstName())
                                    .customerLastName(customer.getLastName())
                                    .sendRef(true)
                                    .pin(transferRequest.getPin())
                                    .transferType(transferRequest.getType())
                                    .build());
                        }
                        // Création et sauvegarde de l'entité de transfert
                        TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
                        transferRequest.getBeneficiaryRequest().setCustomerID(transferRequest.getCustomerID());

                        //transferReceiptWalletToGAB
                        // filling the  transfer recipient  with data:
                        String  beneficiaryFullName =transferRequest.getBeneficiaryRequest().getFirstName()+" "+transferRequest.getBeneficiaryRequest().getLastName();
                        String orderingFullName=customer.getFirstName()+" "+customer.getLastName();
                        double amount=transferRequest.getAmount();
                        String initiatedAt=transferEntity.getInitiatedAt();
                        // Call the TransferReceipt bean and fill it with data
                        transferReceiptBankToGAB.setOrderingFullName(orderingFullName);
                        transferReceiptBankToGAB.setBeneficiaryFullName(beneficiaryFullName );
                        transferReceiptBankToGAB.setAmount(amount);
                        transferReceiptBankToGAB.setInitiatedAt(initiatedAt);
                        transferReceiptBankToGAB.setTransferType("Banque à GAB");
                        transferReceiptBankToGAB.setIsInitiatedFromMobile(transferRequest.getIsInitiatedFromMobile());
                        transferReceiptBankToGAB.setReference(transferEntity.getReference());
                        // Generate the PDF content
                        String newPdfFileName = transferEntity.getReference()+"-recipient.pdf";
                        String newPdfPath = "./TransferService/" + newPdfFileName;
                        transferReceiptBankToGAB.generateTransferReceipt(newPdfPath);
                        // Read the content of the new PDF into a byte array
                        byte[] pdfContent = Files.readAllBytes(Paths.get(newPdfPath));
                        // Set the pdfContent property of transferEntity
                        transferEntity.setPdfContent(pdfContent);
                        // Set the PDF content to the TransferEntity
                        // saving the transfer object
                        transferEntity.setPINCode(transferRequest.getPin());
                        TransferEntity te= transferRepository.save(transferEntity);
//                        producer.sendMessage(transferEntity);
                        BeneficiaryRequest beneficiaryRequest = BeneficiaryRequest.builder()
                                .firstName(transferRequest.getBeneficiaryRequest().getFirstName())
                                .lastName(transferRequest.getBeneficiaryRequest().getLastName())
                                .phone(transferRequest.getBeneficiaryRequest().getPhone())
                                .customerID(transferRequest.getCustomerID())
                                .transferID(te.getId())
                                .cin(transferRequest.getBeneficiaryRequest().getCin())
                                .build();
                        externalClientService.addBeneficiary(beneficiaryRequest);
                        // Réponse de succès
                        return TransferResponse.builder().message("transfert ajouté avec la référence : "
                                + transferEntity.getReference() ).transferID(te.getId()).ref(te.getReference()).isInitaited(true).build();
                    }
                // TODO : Ajouter la logique métier pour BANK_TO_BANK avant le 04/01-->Done
                else if (transferRequest.getType() == TransferType.BANK_TO_BANK) {
                    // Génération d'un numéro de référence pour la demande de transfert
                    transferRequest.setReference(generateReferenceNumber());

                    // Calcul du montant total en incluant les frais
                    if (transferRequest.getIsNotificationsSendingChosen()) {
                        transferRequest.setAmount(transferRequest.getAmount() + 2.00);

                        // Envoi d'un SMS de notification au bénéficiaire
                        externalNotificationService.sendSMS(SMS.builder()
                                .ref(transferRequest.getReference())
                                .amount(transferRequest.getAmounts().get(0))
                                .phone(transferRequest.getBeneficiaryRequest().getPhone())
                                .customerFirstName(customer.getFirstName())
                                .beneficiaryLastName(transferRequest.getBeneficiaryRequest().getLastName())
                                .beneficiaryFirstName(transferRequest.getBeneficiaryRequest().getFirstName())
                                .customerLastName(customer.getLastName())
                                .sendRef(true)
                                .transferType(transferRequest.getType())
                                .build());
                    }

                    // Création et enregistrement de l'entité de transfert
                    TransferEntity transferEntity = buildTransferEntity(transferRequest, customer);
                    transferRequest.getBeneficiaryRequest().setCustomerID(transferRequest.getCustomerID());

                    // Préparation des données pour le reçu de transfert
                    String beneficiaryFullName = transferRequest.getBeneficiaryRequest().getFirstName() + " " + transferRequest.getBeneficiaryRequest().getLastName();
                    String orderingFullName = customer.getFirstName() + " " + customer.getLastName();
                    double amount = transferRequest.getAmount();
                    String initiatedAt = transferEntity.getInitiatedAt();
                    // Remplissage du bean TransferReceipt avec les données
                    transferReceiptBANKToBANK.setOrderingFullName(orderingFullName);
                    transferReceiptBANKToBANK.setBeneficiaryFullName(beneficiaryFullName);
                    transferReceiptBANKToBANK.setAmount(amount);
                    transferReceiptBANKToBANK.setBeneficiaryCin(transferRequest.getBeneficiaryRequest().getCin());
                    transferReceiptBANKToBANK.setOrderingCin(externalClientService.getKYCById(customer.getId()).getIdNumber());
                    transferReceiptBANKToBANK.setInitiatedAt(initiatedAt);
                    transferReceiptBANKToBANK.setTransferType("Banque à banque");
                    transferReceiptBANKToBANK.setIsInitiatedFromMobile(transferRequest.getIsInitiatedFromMobile());
                    transferReceiptBANKToBANK.setReference(transferEntity.getReference());

                    // Génération du contenu PDF pour le reçu
                    String newPdfFileName = transferEntity.getReference() + "-recipient.pdf";
                    String newPdfPath = "./TransferService/" + newPdfFileName;
                    transferReceiptBANKToBANK.generateTransferReceipt(newPdfPath);

                    // Lecture du contenu du PDF et stockage dans un tableau de bytes
                    byte[] pdfContent = Files.readAllBytes(Paths.get(newPdfPath));
                    transferEntity.setPdfContent(pdfContent);

                    // Enregistrement de l'entité de transfert avec le contenu PDF
                    transferEntity.setPINCode(transferRequest.getPin());
                    TransferEntity te = transferRepository.save(transferEntity);

                    // Ajout du bénéficiaire via le service client externe
                    BeneficiaryRequest beneficiaryRequest = BeneficiaryRequest.builder()
                            .firstName(transferRequest.getBeneficiaryRequest().getFirstName())
                            .lastName(transferRequest.getBeneficiaryRequest().getLastName())
                            .phone(transferRequest.getBeneficiaryRequest().getPhone())
                            .customerID(transferRequest.getCustomerID())
                            .transferID(te.getId())
                            .cin(transferRequest.getBeneficiaryRequest().getCin())
                            .build();
                    externalClientService.addBeneficiary(beneficiaryRequest);

                    // Retourner une réponse indiquant le succès du transfert
                    return TransferResponse.builder().message("transfert ajouté avec la référence : "
                            + transferEntity.getReference()).transferID(te.getId()).ref(te.getReference()).isInitaited(true).build();
                }


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
            fixedFee = 9.00;
        } else if (transactionAmount < 10000.00) {
            fixedFee = 49.00;
        } else if (transactionAmount < 20000.00) {
            fixedFee = 199.00;
        } else {
            // Default case, you can set it to 150.00 or any other value as needed
            fixedFee = 399.00;
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
                .PINCode(transferRequest.getPin())
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
@Override
    public byte[] getTransferReceipt(Long transferId) {
        // Retrieve TransferEntity from the database
        TransferEntity transferEntity = transferRepository.findById(transferId).orElse(null);

        if (transferEntity != null) {

            return transferEntity.getPdfContent();
        } else {


            return null;
        }}


    //TODO : Add batch   blocking transfers-->
    //TODO : Handle logic to Block/unblock a transfer -->DONE
    //TODO : Add batch   Restitution transfers : Refund money to wallet
    //TODO : Add batch   Cancel transfers   : refund money to wallet and make and change the status of  Transfer object stored in DB
    @Override
    public void unblockTransfer(Long transferId) {
        TransferEntity transfer = transferRepository.findById(transferId).orElse(null);
        if (transfer != null) {
            transfer.setState(TransferState.TO_BE_SERVED);
            transferRepository.save(transfer);
        }
    }
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
   @Override
   public RestitutionTransferResponse restitutionTransfer(Long transferID) {
       Optional<TransferEntity> transferOpt = transferRepository.findById(transferID);
       if (transferOpt.isEmpty()) {
           return RestitutionTransferResponse.builder()
                   .isRestitutive(false)
                   .customerList(null)
                   .message("Transfer does not exist")
                   .build();
       }

       TransferEntity tr = transferOpt.get();
       List<Object> customers = new ArrayList<>();


       if (tr.getState() == TransferState.RESET) {
           return RestitutionTransferResponse.builder()
                   .customerList(null)
                   .isRestitutive(false)
                   .message("Transfer is already Restituted")
                   .build();
       }

       switch (tr.getType()) {
           case WALLET_TO_WALLET -> {
               Wallet customerWallet = externalClientService.getWalletByWalletID(tr.getCustomerWalletId());
               if (tr.getState() == TransferState.SERVED) {
                   List<Long> beneficiariesIds = tr.getIdsAsList();
                   List<Long> beneficiaryWalletsIds = tr.getWalletIds();
                   List<Double> amounts = tr.getAmounts();

                   for (int i = 0; i < beneficiariesIds.size(); i++) {
                       Long beneficiaryID = beneficiariesIds.get(i);
                       Long beneficiaryWalletID = beneficiaryWalletsIds.get(i);
                       double amount = amounts.get(i);

                       if (externalClientService.getWalletByWalletID(beneficiaryWalletID).getBalance() > amount) {
                           externalClientService.updateWalletBalance(tr.getCustomer().getId(), customerWallet.getBalance() + amount);
                           externalClientService.updateWalletBalance(beneficiaryID, externalClientService.getWalletByWalletID(beneficiaryWalletID).getBalance() - amount);
                           Optional<Customer> c = externalClientService.getCustomerById(beneficiaryID);
                           c.ifPresent(customers::add);
                           tr.setState(TransferState.RESET);
                           transferRepository.save(tr);
                       }
                   }
               }
           }
           case WALLET_TO_BANK, WALLET_TO_GAB -> {
               Wallet customerWallet = externalClientService.getWalletByWalletID(tr.getCustomerWalletId());
               if (tr.getState() == TransferState.TO_BE_SERVED || tr.getState() == TransferState.BLOCKED) {
                   externalClientService.updateWalletBalance(tr.getCustomer().getId(), customerWallet.getBalance() + tr.getAmount());
                   Optional<Customer> c = externalClientService.getCustomerById(tr.getWalletIds().get(0));
                   c.ifPresent(customers::add);
                   tr.setState(TransferState.RESET);
                   transferRepository.save(tr);
               }
           }
           case  BANK_TO_GAB ,BANK_TO_BANK-> {
               if (tr.getState() == TransferState.TO_BE_SERVED || tr.getState() == TransferState.BLOCKED) {
                   Beneficiary c =externalClientService.getBeneficiaryByTransferId(tr.getId());
                   customers.add(c);
                   tr.setState(TransferState.RESET);
                   transferRepository.save(tr);
               }
           }
           default -> {
               return RestitutionTransferResponse.builder()
                       .customerList(null)
                       .isRestitutive(false)
                       .message("Invalid transfer type or state")
                       .build();
           }
       }

       if (customers.isEmpty()) {
           return RestitutionTransferResponse.builder()
                   .customerList(null)
                   .isRestitutive(false)
                   .message("Error while reinstituting transfers: Customers Balance is insufficient")
                   .build();
       }

       return RestitutionTransferResponse.builder()
               .customerList(customers)
               .isRestitutive(true)
               .message("Restitutive Transfers Successful")
               .build();
   }
   @Override
    public List<TransferHistoriesResponse> getTransferHistoriesByCustomerIdNumber(String idNumber) {
        Customer customer = externalClientService.getCustomerByIdNumber(idNumber);
        List<TransferHistoriesResponse> thr = new ArrayList<>();
        List<TransferEntity> transferEntities = transferRepository.findByCustomerId(customer.getId());

        for (TransferEntity tr : transferEntities) {
            List<Recipient> recipients = new ArrayList<>();
            if (tr.getType() == TransferType.WALLET_TO_WALLET) {
                List<Long> beneficiariesIds = tr.getIdsAsList();
                for (Long beneficiaryID : beneficiariesIds) {
                    Optional<Customer> customerOpt = externalClientService.getCustomerById(beneficiaryID);
                    if (customerOpt.isPresent()) {
                        Customer customer2 = customerOpt.get();
                        recipients.add(
                                Recipient.builder()
                                        .cin(customer2.getCin())
                                        .fullName(customer2.getFirstName() + " " + customer2.getLastName())
                                        .build()
                        );
                    }
                }
            } else {
                Beneficiary c = externalClientService.getBeneficiaryByTransferId(tr.getId());
                recipients.add(
                        Recipient.builder()
                                .cin(c.getCin())
                                .fullName(c.getFirstName() + " " + c.getLastName())
                                .build()
                );
            }

            thr.add(
                    TransferHistoriesResponse.builder()
                            .transferedAmount(tr.getAmount())
                            .initatedDate(tr.getInitiatedAt())
                            .type(writeTransferType(tr.getType()))
                            .state(writeTransferState(tr.getState()))  // Assuming this method exists for TransferState
                            .sender(Sender.builder()
                                    .fullName(customer.getFirstName() + " " + customer.getLastName())
                                    .cin(customer.getCin())
                                    .build())
                            .recipient(recipients)
                            .isFound(true)
                            .taransferID(tr.getId())
                            .message("found")
                            .build()
            );
        }

        return thr; // return the list
    }



    public static String writeTransferType(TransferType transferType) {
        return switch (transferType) {
            case WALLET_TO_WALLET -> "Portefeuille à Portefeuille";
            case WALLET_TO_BANK -> "Portefeuille à Banque";
            case BANK_TO_BANK -> "Banque à Banque";
            case WALLET_TO_GAB -> "Portefeuille à GAB";
            case BANK_TO_GAB -> "Banque à GAB";
            default -> "Type Inconnu";
        };
    }
    public static String writeTransferState(TransferState transferState) {
        return switch (transferState) {
            case TO_BE_SERVED -> "A servir";
            case SERVED -> "Servi";
            case EXTOURNED -> "Extourné";
            case RESET -> "Restitué";
            case BLOCKED -> "Bloqué";
            case UNBLOCKED -> "Débloqué";
            case ESCHEAT -> "Déshérence";
            default -> "État Inconnu";
        };
    }
}







