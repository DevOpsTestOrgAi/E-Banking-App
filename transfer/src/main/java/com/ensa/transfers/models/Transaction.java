package com.ensa.transfers.models;

import com.ensa.transfers.enums.TransferServedFrom;
import com.ensa.transfers.enums.TransferType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double commission;
    private Double amount;
    private String senderId;
    private String receiverId;
    private TransferServedFrom transferServedFrom;
    private String pinCode;
    private Duration validationDuration;
    private TransferType type;
    @ManyToOne
    @JoinColumn(name = "transfer_id")
    private Transfer transfer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;
}
