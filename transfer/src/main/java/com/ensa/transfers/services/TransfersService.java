package com.ensa.transfers.services;

import com.ensa.transfers.models.Transfer;
import com.ensa.transfers.repos.TransfersRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransfersService {
    private final TransfersRepo transfersRepo;

    public List<Transfer> getTransfers(){
        return  transfersRepo.findAll();
    }
}
