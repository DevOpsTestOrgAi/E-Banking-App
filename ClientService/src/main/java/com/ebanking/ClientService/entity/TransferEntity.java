package com.ebanking.ClientService.entity;

import com.ebanking.ClientService.model.TransferState;
import com.ebanking.ClientService.model.TransferType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties("beneficiaries")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private double amount;
    private double maxBAmountPeriodC;
    private int maxTransfersPerCustomer;
    private String toBeServedFrom;
    private String PINCode;
    private int maxPIN_Attempts;
    private int validationDuration;
    private String initiatedAt ;
    private  String  receiptUrl;  // change this and store  a binary  pdf
    @Lob
    private byte[] pdfContent;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String motif;

    @Enumerated(EnumType.STRING)
    private TransferState state;

    @Enumerated(EnumType.STRING)
    private TransferType type;

    @OneToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    @JsonIgnoreProperties("transfer")
    private Wallet wallet;
    @OneToMany(mappedBy = "transferEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Beneficiary> beneficiaries;
    private Long customerWalletId ;
    private String  beneficiariesWalletsIds; // Stored as "1|3|5|6|0|"
    private String beneficiariesIds; // Stored as "1|3|5|6|0|"
   // private String amounts; // Stored as "100.00|300.00|"


    public List<Long> getIdsAsList() {
        if (this.beneficiariesIds == null || this.beneficiariesIds.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(this.beneficiariesIds.split("\\|"))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public void addIds(List<Long> newIds) {
        StringBuilder sb = new StringBuilder(this.beneficiariesIds == null ? "" : this.beneficiariesIds);

        for (Long id : newIds) {
            sb.append(id).append("|");
        }

        this.beneficiariesIds = sb.toString();
    }

    public List<Long> getWalletIds() {
        if (this.beneficiariesWalletsIds == null || this.beneficiariesWalletsIds.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(this.beneficiariesWalletsIds.split("\\|"))
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public void addWalletIds(List<Long> newIds) {
        StringBuilder sb = new StringBuilder(this.beneficiariesWalletsIds == null ? "" : this.beneficiariesWalletsIds);

        for (Long id : newIds) {
            sb.append(id).append("|");
        }

        this.beneficiariesWalletsIds = sb.toString();
    }
//    public List<Double> getAmounts() {
//        if (this.amounts == null || this.amounts.isEmpty()) {
//            return new ArrayList<>();
//        }
//        return Arrays.stream(this.amounts.split("\\|"))
//                .filter(s -> !s.isEmpty())
//                .map(Double::parseDouble)
//                .collect(Collectors.toList());
//    }
//
//    public void AddAmounts(List<Double> newAmount) {
//        StringBuilder sb = new StringBuilder(this.amounts == null ? "" : this.amounts);
//
//        for (Double amount : newAmount) {
//            sb.append(amount).append("|");
//        }
//
//        this.amounts = sb.toString();
//    }

}
