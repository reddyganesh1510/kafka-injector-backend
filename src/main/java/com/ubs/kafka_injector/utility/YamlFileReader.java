package com.ubs.kafka_injector.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ubs.kafka_injector.config.KafkaConfig;
import com.ubs.kafka_injector.config.KafkaConfigContainer;
import com.ubs.kafka_injector.config.KafkaConfigWrapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class YamlFileReader {

    public static Map<String, KafkaConfig> loadKafkaConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(new ClassPathResource("kafka-config.yml").getFile(), KafkaConfigWrapper.class).getKafka().getActions();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Kafka config", e);
        }
    }
    public List<String> getKafkaActions() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        KafkaConfigWrapper config = mapper.readValue(new ClassPathResource("kafka-config.yml").getFile(), KafkaConfigWrapper.class);
        return new ArrayList<>(config.getKafka().getActions().keySet());

    }
}
