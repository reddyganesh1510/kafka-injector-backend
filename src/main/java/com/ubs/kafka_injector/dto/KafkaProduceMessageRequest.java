package com.ubs.kafka_injector.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaProduceMessageRequest {
    private String action;
    private String key;
    private String message;
}
