package com.ensa.transfers.models;

import com.ensa.transfers.enums.TransferState;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false, length = 20)
    private String reference;
    private String pinCode;
    private TransferState state;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<TransferHistory> histories;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    public Transfer() {
        this.reference = generateUniqueReference();
    }

    private String generateUniqueReference() {
        long uniqueNumber = System.currentTimeMillis() % 1000000000L;
        return "EDP837" + String.format("%09d", uniqueNumber);
    }
}
