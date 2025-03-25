package com.ubs.kafka_injector.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ubs.kafka_injector.config.KafkaConfig;
import com.ubs.kafka_injector.config.KafkaConfigWrapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Map;

public class YamlFileReader {

    public static Map<String, KafkaConfig> loadKafkaConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(new ClassPathResource("kafka-config.yml").getFile(), KafkaConfigWrapper.class).getKafka().getActions();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Kafka config", e);
        }
    }
}
