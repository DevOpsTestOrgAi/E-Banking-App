//package com.ebanking.ClientService.service;
//
//import com.ebanking.ClientService.entity.SIRONE;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Producer {
//
//    @Value("${topic.name}")
//    private String orderTopic;
//
//    private final ObjectMapper objectMapper;
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    public Producer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    public String sendSIRONE(SIRONE sirone) throws JsonProcessingException {
//        String orderAsMessage = objectMapper.writeValueAsString(sirone);
//        kafkaTemplate.send(orderTopic, orderAsMessage);
//
//        return "message sent";
//    }
//}