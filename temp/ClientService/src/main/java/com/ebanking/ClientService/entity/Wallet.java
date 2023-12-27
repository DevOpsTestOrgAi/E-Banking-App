package com.ebanking.ClientService.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String account_number;
    private String rib;
    private double balance;

    @ManyToOne
    @JoinColumn(name = "customer_id") // Name of the foreign key column in the wallet table
    private Customer customer;

}
