package com.ubs.kafka_injector.config;

import lombok.Getter;
import java.util.Map;

@Getter
public class KafkaConfigContainer {
    private Map<String, KafkaConfig> actions;
}
