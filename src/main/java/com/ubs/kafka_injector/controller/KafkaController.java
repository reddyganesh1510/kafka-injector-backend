package com.ubs.kafka_injector.controller;

import com.ubs.kafka_injector.consumer.DynamicKafkaConsumerService;
import com.ubs.kafka_injector.producer.DynamicKafkaProducerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final DynamicKafkaProducerService dynamicKafkaProducerService;
    private final DynamicKafkaConsumerService dynamicKafkaConsumerService;

    public KafkaController(DynamicKafkaProducerService dynamicKafkaProducerService, DynamicKafkaConsumerService dynamicKafkaConsumerService) {
        this.dynamicKafkaProducerService = dynamicKafkaProducerService;
        this.dynamicKafkaConsumerService = dynamicKafkaConsumerService;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String action,
                              @RequestParam String key,
                              @RequestParam String message) {
        return dynamicKafkaProducerService.sendMessage(action,key,message);
    }

    @GetMapping("/retrieve")
    public List<String> getLastMessages(@RequestParam String action) {
        return dynamicKafkaConsumerService.fetchLastMessages(action);
    }

}
