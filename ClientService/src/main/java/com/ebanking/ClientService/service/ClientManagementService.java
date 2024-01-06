package com.ebanking.ClientService.service;

import com.ebanking.ClientService.entity.Beneficiary;
import com.ebanking.ClientService.entity.Customer;
import com.ebanking.ClientService.entity.KYC;
import com.ebanking.ClientService.entity.Wallet;
import com.ebanking.ClientService.model.*;

import java.util.List;
import java.util.Optional;

public interface ClientManagementService {




    Customer updateCustomer_toCustomer_ID(long customerID, long customerToCustomerID);

    BlackListResponse addCustomerToCustomerByRib(String rib);

    void addToBlackListByCin(String cin, String reason);
    void addToBlackListByRib(String rib, String reason);





    boolean isKYCExpired(Long customerId);
    Optional<Customer> getCustomerById(Long customerId);

    Optional<Wallet> getWalletById(Long customerID);

    AddBeneficiaryResponse addBeneficiary(BeneficiaryRequest beneficiaryRequest);

    List<Beneficiary> getBeneficiariesByCustomerId(Long customerId);

    Optional<Beneficiary> getBeneficiaryById(Long beneficiaryId);

    Optional<Beneficiary> getBeneficiaryByTransferId(Long beneficiaryId);

    void updateTransferID(Long transferID, Long beneficiaryID);

    void updateWalletBalance(Long customerID, double newBalance);

    List<Customer> getAllCustomersByCustomerToCustomerID(Long customerToCustomerID);

    FindCustomerByPhoneResponse checkCustomerSironeStatusAndGetIt(String phone);

    FindCustomerByPhoneResponse checkCustomerSironeStatusAndGetItByRib(String rib);

    SendVerificationCodeResponse  verifyIdentity(SendVerificationCodeRequest sendVerificationCodeRequest);

    FindKYCResponse findKYC(String identity);
    AddKYCResponse addKYC( KYCRequest kycRequest);
    UpdateKYCResponse updateKYCInformation(String cin, KYC updatedKYC);

    List<KYC> getAllKYC();

    // getting the KYC by ID
    Optional<KYC> getKYCById(Long id);


    Wallet getWalletByWalletID(Long id);

    Customer getCustomerByIdNumber(String cin);
}