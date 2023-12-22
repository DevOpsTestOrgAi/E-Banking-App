package com.ebanking.NotificationsService.service;

import com.ebanking.NotificationsService.model.Customer;
import com.ebanking.NotificationsService.model.SMS;
import com.ebanking.NotificationsService.model.TransferType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationsServcieImpl implements NotificationsServcie {
    @Autowired
    Producer producer;

    private final VonageClient vonageClient = VonageClient.builder().apiKey("3073e7dc").apiSecret("ZCZVrdhhblXHWQq1").build();
    private final String BRAND_NAME = "Vonage APIs";

    @Override
    public String sendSMS(SMS sms) {
        TextMessage message = createSMSMessage(sms);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            return message.getMessageBody();
        } else {
            return "Error with sending the message: " + response.getMessages().get(0).getErrorText();
        }
    }

    private TextMessage createSMSMessage(SMS sms) {
        String messageText;

        if (sms.getTransferType() == TransferType.WALLET_TO_GAB||sms.getTransferType() == TransferType. BANK_TO_GAB) {
            String depositInfo = String.format("%s %s a déposé un montant de %.2f DH au GAB au profit de %s %s",
                    sms.getCustomer().getFirstName(), sms.getCustomer().getLastname(),
                    sms.getAmount(), sms.getBeneficiary().getFirstName(), sms.getBeneficiary().getLastName());

            if (sms.getSendRef()) {
                messageText = String.format("%s sous la référence %s. Veuillez utiliser le code de retrait %s.",
                        depositInfo, sms.getRef(), sms.getPin());
            } else {
                messageText = String.format("%s. Veuillez utiliser le code de retrait %s.", depositInfo, sms.getPin());
            }
        } else {
            // Handle other transfer types
            messageText = "Unsupported transfer type";
        }

        return new TextMessage(BRAND_NAME, sms.getBeneficiary().getPhone(), messageText);
    }

    @Override
    public String verifyIdentity(String phone, String code) {
        String msg = "Votre code de vérification est : " + code;
        TextMessage message = new TextMessage(BRAND_NAME, phone, msg);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            return code;
        } else {
            return "Erreur lors de l'envoi du code de vérification : " + code;
        }
    }

    @Override
    public String test(Customer customer) throws JsonProcessingException {
        return producer.sendMessage(customer);
    }
}
