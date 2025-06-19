package com.elyashevich.producer.producer;

import com.elyashevich.producer.exception.BusinessException;
import com.elyashevich.producer.model.MessageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TransactionalMessageProducerImpl implements TransactionalMessageProducer {

    private final KafkaTemplate<String, MessageModel> kafkaTemplate;

    @Value("${app.kafka.topic.name}")
    private String topicName;

    @Override
    @Transactional(transactionManager = "kafkaTransactionManager")
    public void sendTransactionalMessages(String transactionId, int count) {
        for (int i = 1; i <= count; i++) {
            var message = new MessageModel(
                    transactionId,
                    "Transactional message " + i,
                    i
            );

            kafkaTemplate.send(topicName, message.getId(), message);

            if (i == 3 && transactionId.contains("rollback")) {
                throw new BusinessException("Simulated error to trigger transaction rollback");
            }
        }
    }
}