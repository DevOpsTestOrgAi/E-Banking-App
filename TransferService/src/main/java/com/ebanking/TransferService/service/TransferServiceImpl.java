package com.ebanking.TransferService.service;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.TransferEntity;
import com.ebanking.TransferService.external.client.ExternalClientService;
import com.ebanking.TransferService.model.TransferRequest;
import com.ebanking.TransferService.model.TransferResponse;
import com.ebanking.TransferService.model.TransferState;
import com.ebanking.TransferService.repository.BeneficiaryRepository;
import com.ebanking.TransferService.repository.TransferRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
    public class TransferServiceImpl implements TransferService {
         @Autowired
         private ExternalClientService externalClientService;
         @Autowired
         Producer producer ;
         @Autowired
         BeneficiaryRepository beneficiaryRepository;
        private final TransferRepository transferRepository;

        @Autowired
        public TransferServiceImpl(TransferRepository transferRepository) {
            this.transferRepository = transferRepository;
        }

        @Override
        @Transactional
        public TransferResponse initiateTransfer(TransferRequest transferRequest) throws JsonProcessingException {

            TransferEntity transferEntity = TransferEntity.builder()
                    .reference(transferRequest.getReference())
                    .amount(transferRequest.getAmount())
                    .maxBAmountPeriodC(transferRequest.getMaxBAmountPeriodC())
                    .maxTransfersPerCustomer(transferRequest.getMaxTransfersPerCustomer())
                    .toBeServedFrom(transferRequest.getToBeServedFrom())
                    .PINCode(transferRequest.getPINCode())
                    .validationDuration(transferRequest.getValidationDuration())
                    .customer(transferRequest.getCustomer())
                    .beneficiaries(transferRequest.getBeneficiaries())
                    .state(transferRequest.getState())
                    .type(transferRequest.getType())
                    .build();

            transferRepository.save(transferEntity);
            producer.sendMessage(transferEntity);

            return TransferResponse.builder().message("transfer added with ref :"+transferEntity.getReference()).build();

        }

    @Override
    public void serveTransfer(Long transferID) {
        Optional<TransferEntity> optionalTransfer = transferRepository.findById(transferID);
        TransferEntity transferEntity = optionalTransfer.get();

        // Update the transfer state to "SERVED"
        transferEntity.setState(TransferState.SERVED);

        // Save the updated transfer entity
        transferRepository.save(transferEntity);


    }

    @Override
    public Optional<TransferEntity> getTransferByRef(String ref) {
        return transferRepository.findByReference(ref) ;
    }
    @Override
    public List<Beneficiary> getAllBeneficiariesByTransferId(Long transferId) {
        return beneficiaryRepository.findByTransferId(transferId);
    }
    @Override
    public void updateMaxPINAttempts(Long transferId, int newMaxPINAttempts) {
        TransferEntity transfer = transferRepository.findById(transferId).orElse(null);
        if (transfer != null) {
            transfer.setMaxPIN_Attempts(newMaxPINAttempts);
            transferRepository.save(transfer);

        }

        }
    @Override
    public void blockTransfer(Long transferId) {
        TransferEntity transfer = transferRepository.findById(transferId).orElse(null);
        if (transfer != null) {
            transfer.setState(TransferState.BLOCKED);
            transferRepository.save(transfer);
        }
    }

}



