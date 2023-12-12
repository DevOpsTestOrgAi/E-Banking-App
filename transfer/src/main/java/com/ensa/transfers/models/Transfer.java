package com.ensa.transfers.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
}
