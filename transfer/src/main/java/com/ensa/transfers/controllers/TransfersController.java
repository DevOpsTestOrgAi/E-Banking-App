package com.ensa.transfers.controllers;

import com.ensa.transfers.services.TransfersService;
import com.ensa.transfers.models.Transfer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/transfers")
@AllArgsConstructor
public class TransfersController {
    private final TransfersService transfersService;
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Transfer> getTransfers(){
        return  transfersService.getTransfers();
    }
}
