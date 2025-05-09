package com.ubs.kafka_injector.consumer;

import com.ubs.kafka_injector.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.ubs.kafka_injector.utility.YamlFileReader.loadKafkaConfig;

@Slf4j
@Service
public class DynamicKafkaConsumerService {
    private static final Map<String, KafkaConfig> kafkaConfigMap = loadKafkaConfig();

    public List<String> fetchLastMessages(String action, Integer number) {
        KafkaConfig config = kafkaConfigMap.get(action);
        if (config == null) {
            return Collections.singletonList("Invalid action provided!");
        }

        String topic = config.getTopic();
        String bootstrapServers = config.getBootstrapServers();

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "dynamic-consumer-" + UUID.randomUUID()); // Random group ID for fresh consumption
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // Start from the latest if no previous offset
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        List<String> messages = new ArrayList<>();
        int lastNMessages = number;

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            TopicPartition partition = new TopicPartition(topic, 0);
            consumer.assign(Collections.singletonList(partition));

            consumer.seekToEnd(Collections.singletonList(partition));
            long lastOffset = consumer.position(partition);

            long fetchOffset = Math.max(lastOffset - lastNMessages, 0);
            consumer.seek(partition, fetchOffset);

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            for (ConsumerRecord<String, String> record : records) {
                messages.add(record.value());
            }
        }
        return messages.isEmpty() ? Collections.singletonList("No messages found.") : messages;

    }


}
