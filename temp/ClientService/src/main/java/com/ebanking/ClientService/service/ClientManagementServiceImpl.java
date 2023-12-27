package com.ebanking.ClientService.service;

import com.ebanking.ClientService.entity.Customer;
import com.ebanking.ClientService.entity.KYC;
import com.ebanking.ClientService.entity.SIRONE;
import com.ebanking.ClientService.entity.TransferEntity;
import com.ebanking.ClientService.model.BlackListResponse;
import com.ebanking.ClientService.model.CustomerType;
import com.ebanking.ClientService.model.KYCRequest;
import com.ebanking.ClientService.repository.CustomerRepository;
import com.ebanking.ClientService.repository.KYCRepository;
import com.ebanking.ClientService.repository.SIRONERepository;
import com.ebanking.ClientService.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientManagementServiceImpl implements ClientManagementService {
    @Autowired
    private  CustomerRepository customerRepository;
    @Autowired
    private  SIRONERepository sironRepository;
    @Autowired
    private KYCRepository kycRepository;
    @Autowired
    TransferRepository transferRepository;

    @Autowired
    public ClientManagementServiceImpl(CustomerRepository customerRepository, SIRONERepository sironRepository) {
        this.customerRepository = customerRepository;
        this.sironRepository = sironRepository;
    }
    public BlackListResponse checkCustomerSironeStatus(String cin) {
        // Check if the customer exists
        Optional<Customer> customerOptional = customerRepository.findByCin(cin);
        Optional<SIRONE> sironeOptional = sironRepository.findByCin(cin);
        if (customerOptional.isPresent()) {
            // Customer exists, proceed to check SIRONE
            if (sironeOptional.isPresent()) {
                // Customer is in SIRONE blacklist
                return BlackListResponse.builder().isExists(true).isBlocked(true).build();
            } else {
                // Customer is not in SIRONE blacklist
                return BlackListResponse.builder().isExists(true).isBlocked(false).build();
            }
        } else {
            if (sironeOptional.isPresent()) {
                // Customer is in SIRONE blacklist
                return BlackListResponse.builder().isExists(false).isBlocked(true).build();
            } else {
                // Customer is not in SIRONE blacklist
                return BlackListResponse.builder().isExists(false).isBlocked(false).build();
            }
    }}



    public void addToBlackListByCin(String cin, String reason) {
        Optional<SIRONE> existingSirone = sironRepository.findByCin(cin);

        if (existingSirone.isPresent()) {
            // SIRONE entry already exists for the given cin, you may choose to update or throw an exception
            // Here, I'll throw an exception for demonstration purposes
            throw new RuntimeException("Customer is already in the black list.");
        }

        SIRONE sirone = SIRONE.builder().cin(cin).reason(reason).build();
        sironRepository.save(sirone);
    }

    public void addToBlackListByRib(String rib, String reason) {
        Optional<SIRONE> existingSirone = sironRepository.findByRib(rib);

        if (existingSirone.isPresent()) {
            // SIRONE entry already exists for the given rib, you may choose to update or throw an exception
            // Here, I'll throw an exception for demonstration purposes
            throw new RuntimeException("Customer is already in the black list.");
        }

        SIRONE sirone = SIRONE.builder().rib(rib).reason(reason).build();
        sironRepository.save(sirone);
    }
    public void addProspectiveCustomer(KYCRequest kyc) {
        // Check if the customer already exists

        if (kycRepository.existsByIdNumber(kyc.getIdNumber()) ){
            throw new RuntimeException("Customer with CIN " + kyc.getIdNumber() + " already exists.");
        }

        // Save the prospective customer
       Customer customer =new Customer( kyc.getFirstName(), kyc.getLastName(),kyc.getGsm() ,kyc.getIdNumber(),CustomerType.PROSPECT);
        KYC kycInput=KYC.builder()
                .customer(customer)
                .title(kyc.getTitle())
                .firstName(kyc.getFirstName())
                .lastName(kyc.getLastName())
                .idType(kyc.getIdType())
                .countryOfIssue(kyc.getCountryOfIssue())
                .idNumber(kyc.getIdNumber())
                .idExpirationDate(kyc.getIdExpirationDate())
                .idValidityDate(kyc.getIdValidityDate())
                .dateOfBirth(kyc.getDateOfBirth())
                .profession(kyc.getProfession())
                .nationality(kyc.getNationality())
                .countryOfAddress(kyc.getCountryOfAddress())
                .legalAddress(kyc.getLegalAddress())
                .city(kyc.getCity())
                .gsm(kyc.getGsm())
                .email(kyc.getEmail())
                .build();
        kycRepository.save(
                kycInput
        );
        customer.setKyc(kycRepository.findByIdNumber(kyc.getIdNumber()));
    }
    public List<TransferEntity> getAllTransfers() {
        return transferRepository.findAll();
    }
}
