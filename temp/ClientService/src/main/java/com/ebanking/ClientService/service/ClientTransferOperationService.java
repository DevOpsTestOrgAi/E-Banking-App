package com.ebanking.ClientService.service;

import com.ebanking.ClientService.entity.TransferEntity;
import com.ebanking.ClientService.model.TransferWithdrawRequest;
import com.ebanking.ClientService.model.ServeTransferResponse;

public interface ClientTransferOperationService {


    void sendOTP(Long customerID);

    // Mettre le transfert à l'état "A servir"
    void markAsToServe(TransferEntity transfer);

    // Mettre le transfert à l'état "Payé"
    ServeTransferResponse markAsServed(TransferWithdrawRequest transferWithdrawRequest);

    // Annuler un transfert à servir et le mettre à l'état "Extourné"
    void cancelTransfer(TransferEntity transfer);

    // Restituer un transfert à servir et le mettre à l'état "Restitué"
    void returnTransfer(TransferEntity transfer);

    // Bloquer un transfert à servir et le mettre à l'état "Bloqué"
    void blockTransfer(TransferEntity transfer);

    // Débloquer un transfert bloqué et le mettre à l'état "Débloqué à servir"
    void unblockTransfer(TransferEntity transfer);

    // Mettre un transfert en déshérence
    void markAsUnclaimed(TransferEntity transfer);


}
