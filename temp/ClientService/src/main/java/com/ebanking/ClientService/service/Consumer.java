//package com.ebanking.ClientService.service;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class Consumer {
//
//    @Value("${topic.name}")
//    private String topicName;
//
//    private final List<Object> receivedObjects = new ArrayList<>();
//
//    @KafkaListener(topics = "${topic.name}", groupId = "default")
//    public void listen(ConsumerRecord<String, Object> record) {
//        Object receivedObject = record.value();
//
//        // Process the received object
//        System.out.println("Received Object: " + receivedObject);
//
//        // Add the received object to the collection
//        receivedObjects.add(receivedObject);
//
//        // Add your custom logic to handle the received object
//    }
//
//    public List<Object> getAllReceivedObjects() {
//        System.out.println("Returning all received objects: " + receivedObjects);
//        return new ArrayList<>(receivedObjects);
//    }
//}
