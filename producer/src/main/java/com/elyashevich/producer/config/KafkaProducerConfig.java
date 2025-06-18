package com.elyashevich.producer.config;

import com.elyashevich.producer.model.MessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, MessageModel> producerFactory(
            @Value("${app.kafka.transaction-id-prefix}") String transactionIdPrefix,
            ObjectMapper objectMapper
    ) {
        Map<String, Object> configProperties = new HashMap<>();

        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProperties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionIdPrefix + "-1");

        JsonSerializer<MessageModel> serializer = new JsonSerializer<>(objectMapper);
        serializer.setAddTypeInfo(false);

        var factory = new DefaultKafkaProducerFactory<>(
                configProperties,
                new StringSerializer(),
                serializer
        );

        factory.setTransactionIdPrefix(transactionIdPrefix);

        return factory;
    }

    @Bean
    public KafkaTemplate<String, MessageModel> kafkaTemplate(ProducerFactory<String, MessageModel> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}