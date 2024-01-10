package com.ebanking.IdentityProvider.user;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ebanking.IdentityProvider.user.Permission.*;


@RequiredArgsConstructor
public enum Role {

  WALLET(
          Set.of(
                  // Transfer Permissions
                  TRANSFER_INITIATE,
                  TRANSFER_SERVE,
                  TRANSFER_READ,
                  TRANSFER_UPDATE,
                  TRANSFER_BLOCK,
                  TRANSFER_UNBLOCK,
                  TRANSFER_DOWNLOAD_PDF,
                  TRANSFER_RESTITUTE,
                  TRANSFER_HISTORY_READ,

                  //Notifications Permissions
                  SMS_SEND,
                  IDENTITY_VERIFY,

                  //Clients Permissions
                  CLIENT_CHECK_STATUS_BY_CIN,
                  CLIENT_CHECK_STATUS_BY_RIB,
                  CLIENT_ADD_TO_BLACKLIST_BY_CIN,
                  CLIENT_ADD_TO_BLACKLIST_BY_RIB,
                  CLIENT_ADD_PROSPECTIVE,
                  CLIENT_GET_ALL_TRANSFERS,
                  CLIENT_MARK_TRANSFER_SERVED,
                  CLIENT_UPDATE_KYC,
                  CLIENT_ADD_KYC,
                  CLIENT_CHECK_KYC_EXPIRATION,
                  CLIENT_GET_CUSTOMER_BY_ID,
                  CLIENT_GET_WALLET_BY_ID,
                  CLIENT_ADD_BENEFICIARY,
                  CLIENT_GET_BENEFICIARIES,
                  CLIENT_GET_BENEFICIARY_BY_ID,
                  CLIENT_UPDATE_TRANSFER_ID_IN_BENEFICIARY,
                  CLIENT_UPDATE_WALLET_BALANCE,
                  CLIENT_UPDATE_CUSTOMER_TO_CUSTOMER_ID,
                  CLIENT_GET_ALL_CUSTOMERS_BY_CUSTOMER_TO_CUSTOMER_ID,
                  CLIENT_CHECK_CUSTOMER_SIRONE_STATUS,
                  CLIENT_CHECK_CUSTOMER_SIRONE_STATUS_BY_RIB,
                  CLIENT_SEND_OTP,
                  CLIENT_FIND_KYC,
                  CLIENT_GET_ALL_KYC,
                  CLIENT_GET_KYC_BY_ID,
                  CLIENT_GET_WALLET_BY_WALLET_ID,
                  CLIENT_GET_BENEFICIARY_BY_TRANSFER_ID,
                  CLIENT_GET_CUSTOMER_BY_ID_NUMBER,
                  CLIENT_TEST

          )
  ),
  BACK_OFFICE(
          Set.of(
                  // Transfer Permissions
                  TRANSFER_INITIATE,
                  TRANSFER_SERVE,
                  TRANSFER_READ,
                  TRANSFER_UPDATE,
                  TRANSFER_BLOCK,
                  TRANSFER_UNBLOCK,
                  TRANSFER_DOWNLOAD_PDF,
                  TRANSFER_RESTITUTE,
                  TRANSFER_HISTORY_READ,

                  //Notifications Permissions
                  SMS_SEND,
                  IDENTITY_VERIFY,

                  //Clients Permissions
                  CLIENT_CHECK_STATUS_BY_CIN,
                  CLIENT_CHECK_STATUS_BY_RIB,
                  CLIENT_ADD_TO_BLACKLIST_BY_CIN,
                  CLIENT_ADD_TO_BLACKLIST_BY_RIB,
                  CLIENT_ADD_PROSPECTIVE,
                  CLIENT_GET_ALL_TRANSFERS,
                  CLIENT_MARK_TRANSFER_SERVED,
                  CLIENT_UPDATE_KYC,
                  CLIENT_ADD_KYC,
                  CLIENT_CHECK_KYC_EXPIRATION,
                  CLIENT_GET_CUSTOMER_BY_ID,
                  CLIENT_GET_WALLET_BY_ID,
                  CLIENT_ADD_BENEFICIARY,
                  CLIENT_GET_BENEFICIARIES,
                  CLIENT_GET_BENEFICIARY_BY_ID,
                  CLIENT_UPDATE_TRANSFER_ID_IN_BENEFICIARY,
                  CLIENT_UPDATE_WALLET_BALANCE,
                  CLIENT_UPDATE_CUSTOMER_TO_CUSTOMER_ID,
                  CLIENT_GET_ALL_CUSTOMERS_BY_CUSTOMER_TO_CUSTOMER_ID,
                  CLIENT_CHECK_CUSTOMER_SIRONE_STATUS,
                  CLIENT_CHECK_CUSTOMER_SIRONE_STATUS_BY_RIB,
                  CLIENT_SEND_OTP,
                  CLIENT_FIND_KYC,
                  CLIENT_GET_ALL_KYC,
                  CLIENT_GET_KYC_BY_ID,
                  CLIENT_GET_WALLET_BY_WALLET_ID,
                  CLIENT_GET_BENEFICIARY_BY_TRANSFER_ID,
                  CLIENT_GET_CUSTOMER_BY_ID_NUMBER,
                  CLIENT_TEST
                  )
  ),
  AGENT(
          Set.of(
                  // Transfer Permissions
                  TRANSFER_INITIATE,
                  TRANSFER_SERVE,
                  TRANSFER_READ,
                  TRANSFER_UPDATE,
                  TRANSFER_BLOCK,
                  TRANSFER_UNBLOCK,
                  TRANSFER_DOWNLOAD_PDF,
                  TRANSFER_RESTITUTE,
                  TRANSFER_HISTORY_READ,

                  //Notifications Permissions
                  SMS_SEND,
                  IDENTITY_VERIFY,

                  //Clients Permissions
                  CLIENT_CHECK_STATUS_BY_CIN,
                  CLIENT_CHECK_STATUS_BY_RIB,
                  CLIENT_ADD_TO_BLACKLIST_BY_CIN,
                  CLIENT_ADD_TO_BLACKLIST_BY_RIB,
                  CLIENT_ADD_PROSPECTIVE,
                  CLIENT_GET_ALL_TRANSFERS,
                  CLIENT_MARK_TRANSFER_SERVED,
                  CLIENT_UPDATE_KYC,
                  CLIENT_ADD_KYC,
                  CLIENT_CHECK_KYC_EXPIRATION,
                  CLIENT_GET_CUSTOMER_BY_ID,
                  CLIENT_GET_WALLET_BY_ID,
                  CLIENT_ADD_BENEFICIARY,
                  CLIENT_GET_BENEFICIARIES,
                  CLIENT_GET_BENEFICIARY_BY_ID,
                  CLIENT_UPDATE_TRANSFER_ID_IN_BENEFICIARY,
                  CLIENT_UPDATE_WALLET_BALANCE,
                  CLIENT_UPDATE_CUSTOMER_TO_CUSTOMER_ID,
                  CLIENT_GET_ALL_CUSTOMERS_BY_CUSTOMER_TO_CUSTOMER_ID,
                  CLIENT_CHECK_CUSTOMER_SIRONE_STATUS,
                  CLIENT_CHECK_CUSTOMER_SIRONE_STATUS_BY_RIB,
                  CLIENT_SEND_OTP,
                  CLIENT_FIND_KYC,
                  CLIENT_GET_ALL_KYC,
                  CLIENT_GET_KYC_BY_ID,
                  CLIENT_GET_WALLET_BY_WALLET_ID,
                  CLIENT_GET_BENEFICIARY_BY_TRANSFER_ID,
                  CLIENT_GET_CUSTOMER_BY_ID_NUMBER,
                  CLIENT_TEST

          )
  )

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
