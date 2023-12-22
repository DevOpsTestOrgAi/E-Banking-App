package com.ebanking.ClientService.service;

import com.ebanking.ClientService.model.BlackListResponse;

public interface ClientManagementService {

    BlackListResponse checkCustomerSironeStatus(String cin);
    void addToBlackListByCin(String cin, String reason);
    void addToBlackListByRib(String rib, String reason);

}