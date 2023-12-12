package com.ensa.customers.models;

import com.ensa.customers.enums.Role;
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
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @ElementCollection
    private List<String> transactionIds;
    @OneToOne(cascade = CascadeType.ALL)
    private KYCData kycData;
    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    @ElementCollection
    private List<String> notificationsIds;
    private Boolean isNotificationModeOn;
    private Role role;
    @ManyToOne
    @JoinColumn(name = "siron_id")
    private Siron siron;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

}
