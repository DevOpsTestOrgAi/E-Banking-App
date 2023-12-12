package com.ensa.transfers.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prospect {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long senderId;
    private Long receiverId;
    private Long giverKYCDataId;
    private Long receiverKYCDataId;
}
