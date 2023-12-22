package com.ebanking.TransferService.entity;
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
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transfer_id")
    private TransferEntity transfer;
    private  String  firstName ;
    private  String  lastname ;
    private  String  phone ;
    private  String   rib  ;
    private  String  cin  ;




}