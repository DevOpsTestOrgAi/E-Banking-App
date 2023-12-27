package com.ebanking.NotificationsService.service;

import com.ebanking.NotificationsService.entity.Beneficiary;
import com.ebanking.NotificationsService.model.Customer;
import com.ebanking.NotificationsService.model.SMS;
import com.ebanking.NotificationsService.model.SendVerificationCodeResponse;
import com.ebanking.NotificationsService.model.TransferType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationsServcieImpl implements NotificationsServcie {
    @Autowired
    Producer producer;

    private final VonageClient vonageClient = VonageClient.builder().apiKey("3073e7dc").apiSecret("ZCZVrdhhblXHWQq1").build();
    private final String BRAND_NAME = "Vonage APIs";

    @Override
    public String sendSMS(SMS sms) {
        List<TextMessage> messages = createSMSMessages(sms);

        List<String> results = new ArrayList<>();
        for (TextMessage message : messages) {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);
            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                results.add("Message sent successfully to " + message.getTo());
            } else {
                results.add("Error with sending the message to " + message.getTo() + ": " +
                        response.getMessages().get(0).getErrorText());
            }
        }

        return String.join("\n", results);
    }

    private List<TextMessage> createSMSMessages(SMS sms) {
        if (sms.getTransferType() == TransferType.WALLET_TO_GAB || sms.getTransferType() == TransferType.BANK_TO_GAB) {
            return sms.getBeneficiaries().stream()
                    .map(beneficiary -> createSingleSMSMessage(sms, beneficiary))
                    .collect(Collectors.toList());
        } else {
            // Handle other transfer types
            throw new UnsupportedOperationException("Unsupported transfer type");
        }
    }

    private TextMessage createSingleSMSMessage(SMS sms, Beneficiary beneficiary) {
        String depositInfo = String.format("%s %s a déposé un montant de %.2f DH au GAB au profit de %s %s",
                sms.getCustomer().getFirstName(), sms.getCustomer().getLastname(),
                sms.getAmount(), beneficiary.getFirstName(), beneficiary.getLastName());

        String messageText;
        if (sms.getSendRef()) {
            messageText = String.format("%s sous la référence %s. Veuillez utiliser le code de retrait %s.",
                    depositInfo, sms.getRef(), sms.getPin());
        } else {
            messageText = String.format("%s. Veuillez utiliser le code de retrait %s.", depositInfo, sms.getPin());
        }

        return new TextMessage(BRAND_NAME, beneficiary.getPhone(), messageText);
    }


    @Override
    public SendVerificationCodeResponse verifyIdentity(String phone, String code) {
        String msg = "Votre code de vérification est : " + code;
        TextMessage message = new TextMessage(BRAND_NAME, phone, msg);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            return SendVerificationCodeResponse.builder().message(msg).code(code).build();
        } else {
            return SendVerificationCodeResponse.builder().message("error  occurred while sending the verification code "
                    ).code(code).build();
        }
    }

    @Override
    public String test(Customer customer) throws JsonProcessingException {
        return producer.sendMessage(customer);
    }
}
