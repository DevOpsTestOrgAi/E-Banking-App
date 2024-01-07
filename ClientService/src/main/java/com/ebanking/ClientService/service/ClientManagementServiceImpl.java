package com.ebanking.ClientService.service;

import com.ebanking.ClientService.entity.*;
import com.ebanking.ClientService.external.client.ExternalNotificationService;
import com.ebanking.ClientService.model.*;
import com.ebanking.ClientService.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
    WalletRepository walletRepository;
    @Autowired
    BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private ExternalNotificationService externalNotificationService;
    public static String generate4DigitNumber() {
        // Generate a random 4-digit number
        Random random = new Random();
        int randomValue = 1000 + random.nextInt(9000);

        // Convert the number to a string and return
        return String.valueOf(randomValue);
    }


    @Autowired
    public ClientManagementServiceImpl(CustomerRepository customerRepository, SIRONERepository sironRepository) {
        this.customerRepository = customerRepository;
        this.sironRepository = sironRepository;
    }
    public BlackListResponse checkCustomerSironeStatus(String phone) {
        // Check if the customer exists
        Optional<Customer> customerOptional = customerRepository.findByPhone(phone);
        Optional<SIRONE> sironeOptional = sironRepository.findByPhone(phone);
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
    }
    }

@Override
public Customer updateCustomer_toCustomer_ID(long customerID, long customerToCustomerID){
        Customer customer = customerRepository.getReferenceById(customerID);
       customer.setCtc(customerToCustomerID);
       return  customerRepository.save(customer);
   }

    @Override
    public BlackListResponse addCustomerToCustomerByRib(String rib) {
        // Check if the customer exists
        Optional<Customer> customerOptional = customerRepository.findByRib(rib);
        Optional<SIRONE> sironeOptional = sironRepository.findByRib(rib);
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
        Optional<KYC> kyc1=kycRepository.findByIdNumber(kyc.getIdNumber());
        if(kyc1.isPresent()){
            KYC kyc2 =kyc1.get();
            customer.setKyc(kyc2);
        }

    }
    public List<TransferEntity> getAllTransfers() {
        return transferRepository.findAll();
    }
    @Transactional
    public UpdateKYCResponse updateKYCInformation(String cin, KYC updatedKYC) {
        Optional<Customer> optionalCustomer = customerRepository.findByCin(cin);
        if(optionalCustomer.isPresent()){
            Customer customer  = optionalCustomer.get();
            KYC newKyc = KYC.builder()
                    .id(customer.getKyc().getId()) // Existing ID
                    .title(updatedKYC.getTitle())
                    .firstName(updatedKYC.getFirstName())
                    .lastName(updatedKYC.getLastName())
                    .idType(updatedKYC.getIdType())
                    .countryOfIssue(updatedKYC.getCountryOfIssue())
                    .idNumber(updatedKYC.getIdNumber())
                    .idExpirationDate(updatedKYC.getIdExpirationDate())
                    .idValidityDate(updatedKYC.getIdValidityDate())
                    .dateOfBirth(updatedKYC.getDateOfBirth())
                    .profession(updatedKYC.getProfession())
                    .nationality(updatedKYC.getNationality())
                    .countryOfAddress(updatedKYC.getCountryOfAddress())
                    .legalAddress(updatedKYC.getLegalAddress())
                    .city(updatedKYC.getCity())
                    .gsm(updatedKYC.getGsm())
                    .email(updatedKYC.getEmail())
                    // Assuming customer field should remain linked to the same customer
                    .customer(customer)
                    .build();
            customer.setKyc(newKyc);
            customerRepository.save(customer);
            return UpdateKYCResponse.builder()
                    .isUpdated(true)
                    .message("KYC is updated")
                    .build();

        }else{
            return UpdateKYCResponse.builder()
                    .isUpdated(true)
                    .message("KYC Not found ")
                    .build();
        }


    }

    private void updateKYCFields(KYC existingKYC, KYC updatedKYC) {
        existingKYC.setTitle(updatedKYC.getTitle());
        existingKYC.setFirstName(updatedKYC.getFirstName());
        existingKYC.setLastName(updatedKYC.getLastName());
        existingKYC.setIdType(updatedKYC.getIdType());
        existingKYC.setCountryOfIssue(updatedKYC.getCountryOfIssue());
        existingKYC.setIdNumber(updatedKYC.getIdNumber());
        existingKYC.setIdExpirationDate(updatedKYC.getIdExpirationDate());
        existingKYC.setIdValidityDate(updatedKYC.getIdValidityDate());
        existingKYC.setDateOfBirth(updatedKYC.getDateOfBirth());
        existingKYC.setProfession(updatedKYC.getProfession());
        existingKYC.setNationality(updatedKYC.getNationality());
        existingKYC.setCountryOfAddress(updatedKYC.getCountryOfAddress());
        existingKYC.setLegalAddress(updatedKYC.getLegalAddress());
        existingKYC.setCity(updatedKYC.getCity());
        existingKYC.setGsm(updatedKYC.getGsm());
        existingKYC.setEmail(updatedKYC.getEmail());
    }

    private boolean isIdExpired(String idExpirationDate) {
        // Parse the expiration date string into LocalDate
        LocalDate expirationDate = LocalDate.parse(idExpirationDate);

        // Compare with the current date
        return LocalDate.now().isAfter(expirationDate);
    }
@Override
    public  AddKYCResponse addKYC(KYCRequest kycRequest) {
        // adding the KYC object from teh request
        Optional<KYC> kyc=kycRepository.findByIdNumber(kycRequest.getIdNumber());
if(kyc.isEmpty()) {
    KYC newKYC = buildKYCFromRequest(kycRequest);
    KYC savedKYC = kycRepository.save(newKYC);
    Customer customer = Customer.builder()
            .cin(savedKYC.getIdNumber())
            .customerType(CustomerType.PROSPECT)
            .phone(savedKYC.getGsm())
            .firstName(savedKYC.getFirstName())
            .lastName(savedKYC.getLastName())
            .kyc(savedKYC)
            .build();
    return AddKYCResponse.builder()
            .isAdded(true)
            .customer(customerRepository.save(customer))
            .message(" Le client Prospect a été ajouté avec succès.")
            .build();

}else
{
    return      AddKYCResponse.builder()
        .isAdded(false)

        .message("  Un client existe déjà avec le numéro d'identification fourni.")
        .build();

}

    }

    private KYC buildKYCFromRequest(KYCRequest kycRequest) {
        return KYC.builder()
                .title(kycRequest.getTitle())
                .firstName(kycRequest.getFirstName())
                .lastName(kycRequest.getLastName())
                .idType(kycRequest.getIdType())
                .countryOfIssue(kycRequest.getCountryOfIssue())
                .idNumber(kycRequest.getIdNumber())
                .idExpirationDate(kycRequest.getIdExpirationDate())
                .idValidityDate(kycRequest.getIdValidityDate())
                .dateOfBirth(kycRequest.getDateOfBirth())
                .profession(kycRequest.getProfession())
                .nationality(kycRequest.getNationality())
                .countryOfAddress(kycRequest.getCountryOfAddress())
                .legalAddress(kycRequest.getLegalAddress())
                .city(kycRequest.getCity())
                .gsm(kycRequest.getGsm())
                .email(kycRequest.getEmail())
                .build();
    }

    public boolean isKYCExpired(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer != null && customer.getKyc() != null) {
            KYC kyc = customer.getKyc();

            // Assuming the date format is yyyy-MM-dd
            LocalDate expirationDate = LocalDate.parse(kyc.getIdExpirationDate());
            LocalDate currentDate = LocalDate.now();

            // Check if the KYC is expired
            return currentDate.isAfter(expirationDate);
        }

        return false; // Return false if KYC or customer not found
    }
    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }
    @Override
    public Optional<Wallet> getWalletById(Long customerID) {
        return walletRepository.findByCustomerId(customerID);
    }
    @Override
    public AddBeneficiaryResponse addBeneficiary(BeneficiaryRequest beneficiaryRequest) {
        Beneficiary beneficiary=Beneficiary.builder()
                .cin(beneficiaryRequest.getCin())
                .phone(beneficiaryRequest.getPhone())
                .firstName(beneficiaryRequest.getFirstName())
                .lastName(beneficiaryRequest.getLastName())
                .customerID(beneficiaryRequest.getCustomerID())
                .transferID(beneficiaryRequest.getTransferID())
                .build();
        Beneficiary bn =beneficiaryRepository.save(beneficiary);
        return  AddBeneficiaryResponse.builder()
                .id(bn.getId())
                .message("beneficiary is added successfully")
                .build();



    }
    @Override
    public List<Beneficiary> getBeneficiariesByCustomerId(Long customerId) {
        return beneficiaryRepository.findByCustomerId(customerId);
    }
    @Override
    public Optional<Beneficiary> getBeneficiaryById(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId);
    }
    @Override
    public Optional<Beneficiary> getBeneficiaryByTransferId(Long beneficiaryId) {
        return beneficiaryRepository.findByTransferID(beneficiaryId);
    }
    @Override
    public void updateTransferID(Long transferID, Long beneficiaryID) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryID)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with ID: " + beneficiaryID));

        beneficiary.setTransferID(transferID);

        beneficiaryRepository.save(beneficiary);
    }
    @Override
    public void updateWalletBalance(Long customerID, double newBalance) {
        Wallet wallet =  walletRepository.findByCustomer_Id(customerID)
                .orElseThrow(() -> new RuntimeException("Wallet not found for customer with ID: " + customerID));

        wallet.setBalance(newBalance);

        walletRepository.save(wallet);
    }
    @Override
    public List<Customer> getAllCustomersByCustomerToCustomerID(Long customerToCustomerID) {
        return customerRepository.findAllByCtc(customerToCustomerID);
    }
    @Override
    public FindCustomerByPhoneResponse checkCustomerSironeStatusAndGetIt(String phone) {
        // Check if the customer exists
        Optional<Customer> customerOptional = customerRepository.findByPhone(phone);
        Optional<SIRONE> sironeOptional = sironRepository.findByPhone(phone);

        if (customerOptional.isPresent()) {
            // Customer exists, proceed to check SIRONE
            if (sironeOptional.isPresent()) {
                // Customer is in SIRONE blacklist
                return FindCustomerByPhoneResponse.builder()
                        .message("Customer is in SIRONE blacklist.")
                        .isBlockedOrExist(false)
                        .build();
            } else {
                // Customer is not in SIRONE blacklist
                return FindCustomerByPhoneResponse.builder()
                        .customer(customerOptional.get())
                        .message("Customer exists and is not in blacklist.")
                        .isBlockedOrExist(true)
                        .build();
            }
        } else {
            if (sironeOptional.isPresent()) {
                // Customer is in SIRONE blacklist
                return FindCustomerByPhoneResponse.builder()
                        .message("Customer is in SIRONE blacklist.")
                        .isBlockedOrExist(false)
                        .build();
            } else {
                // Customer is not in SIRONE blacklist
                return FindCustomerByPhoneResponse.builder()
                        .message("Customer does not exist .")
                        .isBlockedOrExist(false)
                        .build();
            }
        }
    }
    @Override
    public FindCustomerByPhoneResponse checkCustomerSironeStatusAndGetItByRib(String rib) {
        // Check if the customer exists
        Optional<Customer> customerOptional = customerRepository.findByRib(rib);
        Optional<SIRONE> sironeOptional = sironRepository.findByRib(rib);

        if (customerOptional.isPresent()) {
            Customer c =customerOptional.get();
            // Customer exists, proceed to check SIRONE
            if (sironeOptional.isPresent()) {
                // Customer is in SIRONE blacklist
                return FindCustomerByPhoneResponse.builder()
                        .message("Customer is in SIRONE blacklist.")
                        .isBlockedOrExist(false)
                        .build();
            } else {
                // Customer is not in SIRONE blacklist
                return FindCustomerByPhoneResponse.builder()
                        .customer(customerOptional.get())
                        .message("Customer exists and is not in SIRONE blacklist.")
                        .isBlockedOrExist(true)
                        .id(c.getId())
                        .build();
            }
        } else {
            if (sironeOptional.isPresent()) {
                // Customer is in SIRONE blacklist
                return FindCustomerByPhoneResponse.builder()
                        .message("Customer is in SIRONE blacklist.")
                        .isBlockedOrExist(false)
                        .build();
            } else {
                // Customer is not in SIRONE blacklist
                return FindCustomerByPhoneResponse.builder()
                        .message("Customer does not exist.")
                        .isBlockedOrExist(false)
                        .build();
            }
        }
    }
@Override
    public SendVerificationCodeResponse  verifyIdentity(SendVerificationCodeRequest sendVerificationCodeRequest){
        String code =generate4DigitNumber();
        String phone  = sendVerificationCodeRequest.getPhone();
        Optional<Customer> customer= customerRepository.findByPhone(phone);
    if (customer.isPresent()) {
        Customer c = customer.get();
        c.setOtp(code);
        customerRepository.save(c);
    }
  return externalNotificationService.verifyIdentity(phone,code).getBody();

}
    @Override
    public FindKYCResponse findKYC(String identity) {
        Optional<Customer> customer = customerRepository.findByCin(identity);
        if (customer.isPresent()) {
            Customer c = customer.get();
            KYC kyc = c.getKyc();
            // Assuming the date format is yyyy-MM-dd
            LocalDate expirationDate = LocalDate.parse(kyc.getIdExpirationDate());
            LocalDate currentDate = LocalDate.now();
            // Check if the KYC is expired
            if (currentDate.isAfter(expirationDate)) {
                return FindKYCResponse.builder()
                        .isFound(true)
                        .message("La vérification KYC a expiré, veuillez la mettre à jour.")
                        .kyc(kyc)
                        .isExpired(true)
                        .build();
            } else {
                return FindKYCResponse.builder()
                        .isFound(true)
                        .message("Voici le Kyc")
                        .kyc(kyc)
                        .isExpired(false)
                        .build();
            }
        }
   else {
        return FindKYCResponse.builder()
                .isFound(false)
                .message("Le numéro d'identité n'est pas trouvé dans la liste KYC.")
                .build();
    }

}
@Override
    public List<KYC> getAllKYC() {
        return kycRepository.findAll();
    }
    // getting the KYC by ID
    @Override
    public Optional<KYC> getKYCById(Long id) {
        return kycRepository.findById(id);
    }

  @Override
    public Wallet getWalletByWalletID(Long id){

        Optional<Wallet> wallet = walletRepository.findById(id);
      return wallet.orElse(null);

  }
  @Override
  public  Customer getCustomerByIdNumber(String cin){
        Optional<Customer> customer =customerRepository.findByCin(cin);
      return customer.orElse(null);
  }
}