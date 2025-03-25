package com.ubs.kafka_injector.producer;
import com.ubs.kafka_injector.config.KafkaConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.ubs.kafka_injector.utility.YamlFileReader.loadKafkaConfig;


@Service
public class DynamicKafkaProducerService {
    private static final Map<String, KafkaConfig> kafkaConfigMap = loadKafkaConfig();

    public String sendMessage(String action , String key, String message){
        KafkaConfig config = kafkaConfigMap.get(action);
        if (config == null) {
            return "Invalid action provided!";
        }
        String bootstrapServers = config.getBootstrapServers();
        String topic = config.getTopic();
        KafkaTemplate<String, String> kafkaTemplate = createKafkaTemplate(bootstrapServers);
        kafkaTemplate.send(topic, key, message);
        return "Message sent successfully on " + topic;
    }
    private KafkaTemplate<String, String> createKafkaTemplate(String bootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        return new KafkaTemplate<>(producerFactory);
    }
}