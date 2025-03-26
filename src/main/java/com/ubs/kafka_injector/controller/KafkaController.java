package com.ubs.kafka_injector.controller;

import com.ubs.kafka_injector.consumer.DynamicKafkaConsumerService;
import com.ubs.kafka_injector.dto.KafkaProduceMessageRequest;
import com.ubs.kafka_injector.producer.DynamicKafkaProducerService;
import com.ubs.kafka_injector.utility.YamlFileReader;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final DynamicKafkaProducerService dynamicKafkaProducerService;
    private final DynamicKafkaConsumerService dynamicKafkaConsumerService;
    private final YamlFileReader yamlFileReader;



    public KafkaController(DynamicKafkaProducerService dynamicKafkaProducerService, DynamicKafkaConsumerService dynamicKafkaConsumerService, YamlFileReader yamlFileReader) {
        this.dynamicKafkaProducerService = dynamicKafkaProducerService;
        this.dynamicKafkaConsumerService = dynamicKafkaConsumerService;
        this.yamlFileReader = yamlFileReader;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody KafkaProduceMessageRequest request) {
        return dynamicKafkaProducerService.sendMessage(request.getAction(), request.getKey(), request.getMessage());
    }

    @GetMapping("/retrieve")
    public List<String> getLastMessages(@RequestParam String action, @RequestParam Integer number) {
        return dynamicKafkaConsumerService.fetchLastMessages(action, number);
    }

    @GetMapping("/actions")
    public List<String> getKafkaActions() throws IOException {
        return yamlFileReader.getKafkaActions();
    }
}
