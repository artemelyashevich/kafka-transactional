package com.elyashevich.producer.producer;

import com.elyashevich.producer.model.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalMessageProducerImpl implements TransactionalMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topicName;

    public TransactionalMessageProducerImpl(KafkaTemplate<String, Object> kafkaTemplate,
                                            @Value("${app.kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    @Transactional
    public void sendTransactionalMessages(String transactionId, int count) {
        for (int i = 1; i <= count; i++) {
            var message = new MessageModel(
                transactionId,
                "Transactional message " + i,
                i
            );

            kafkaTemplate.send(topicName, message.getId(), message);

            if (i == 3 && transactionId.contains("rollback")) {
                throw new RuntimeException("Simulated error to trigger transaction rollback");
            }
        }
    }
}