package com.ebanking.ClientService.entity;
import com.ebanking.ClientService.model.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String cin;
    private String firstName;
    private String lastName;
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kyc_id", referencedColumnName = "id")
    private KYC kyc;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferEntity> transactions;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Wallet> wallets;
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    public Customer(String firstName, String lastName, String gsm, String idNumber, CustomerType type) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=gsm;
        this.cin= idNumber ;
        this.customerType=type;
    }

    // Constructors, getters, setters...
}
