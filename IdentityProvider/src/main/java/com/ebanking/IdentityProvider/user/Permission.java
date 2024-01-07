package com.ebanking.IdentityProvider.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    TRANSFER_INITIATE("TRANSFER:initiate"),
    TRANSFER_SERVE("TRANSFER:serve"),
    TRANSFER_READ("TRANSFER:read"),
    TRANSFER_UPDATE("TRANSFER:update"),
    TRANSFER_BLOCK("TRANSFER:block"),
    TRANSFER_UNBLOCK("TRANSFER:unblock"),
    TRANSFER_DOWNLOAD_PDF("TRANSFER:downloadPdf"),
    TRANSFER_RESTITUTE("TRANSFER:restitute"),
    TRANSFER_HISTORY_READ("TRANSFER:historyRead"),


    SMS_SEND("NOTIFICATIONS:sendSMS"),
    IDENTITY_VERIFY("NOTIFICATIONS:verifyIdentity"),

    CLIENT_CHECK_STATUS_BY_CIN("CLIENT:checkStatusByCin"),
    CLIENT_CHECK_STATUS_BY_RIB("CLIENT:checkStatusByRib"),
    CLIENT_ADD_TO_BLACKLIST_BY_CIN("CLIENT:addToBlackListByCin"),
    CLIENT_ADD_TO_BLACKLIST_BY_RIB("CLIENT:addToBlackListByRib"),
    CLIENT_ADD_PROSPECTIVE("CLIENT:addProspectiveCustomer"),
    CLIENT_GET_ALL_TRANSFERS("CLIENT:getAllTransfers"),
    CLIENT_MARK_TRANSFER_SERVED("CLIENT:markTransferAsServed"),
    CLIENT_UPDATE_KYC("CLIENT:updateKYCInformation"),
    CLIENT_ADD_KYC("CLIENT:addKYC"),
    CLIENT_CHECK_KYC_EXPIRATION("CLIENT:check-KYC-expiration"),
    CLIENT_GET_CUSTOMER_BY_ID("CLIENT:getCustomerById"),
    CLIENT_GET_WALLET_BY_ID("CLIENT:getWalletById"),
    CLIENT_ADD_BENEFICIARY("CLIENT:addBeneficiary"),
    CLIENT_GET_BENEFICIARIES("CLIENT:findAllBeneficiariesByCustomerId"),
    CLIENT_GET_BENEFICIARY_BY_ID("CLIENT:findBeneficiaryById"),
    CLIENT_UPDATE_TRANSFER_ID_IN_BENEFICIARY("CLIENT:updateTransferIDInBeneficiary"),
    CLIENT_UPDATE_WALLET_BALANCE("CLIENT:update-balance"),
    CLIENT_UPDATE_CUSTOMER_TO_CUSTOMER_ID("CLIENT:update-customer-to-customer-id"),
    CLIENT_GET_ALL_CUSTOMERS_BY_CUSTOMER_TO_CUSTOMER_ID("CLIENT:get-by-customer-to-customer-id"),
    CLIENT_CHECK_CUSTOMER_SIRONE_STATUS("CLIENT:check-customer-sirone-status"),
    CLIENT_CHECK_CUSTOMER_SIRONE_STATUS_BY_RIB("CLIENT:check-customer-sirone-status-by-rib"),
    CLIENT_SEND_OTP("CLIENT:sendOTP"),
    CLIENT_FIND_KYC("CLIENT:find-kyc"),
    CLIENT_GET_ALL_KYC("CLIENT:GetAllKyc"),
    CLIENT_GET_KYC_BY_ID("CLIENT:getKycByID"),
    CLIENT_GET_WALLET_BY_WALLET_ID("CLIENT:getWalletByWalletID"),
    CLIENT_GET_BENEFICIARY_BY_TRANSFER_ID("CLIENT:findBeneficiaryByTransferID"),
    CLIENT_GET_CUSTOMER_BY_ID_NUMBER("CLIENT:findCustomerByIdNumber"),
    CLIENT_TEST("CLIENT:test"),


    ;

    @Getter
    private final String permission;
}
