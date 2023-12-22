package com.ebanking.TransferService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KYCRequest {

    private String title;
    private String firstName;
    private String lastName;
    private String idType;
    private String countryOfIssue;
    private String idNumber;
    private String idExpirationDate;
    private String idValidityDate;
    private String dateOfBirth;
    private String profession;
    private String nationality;
    private String countryOfAddress;
    private String legalAddress;
    private String city;
    private String gsm;
    private String email;
}
