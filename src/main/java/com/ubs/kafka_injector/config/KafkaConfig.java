package com.ubs.kafka_injector.config;


import lombok.Getter;

@Getter
public class KafkaConfig {
    private String bootstrapServers;
    private String topic;
}
