package com.elyashevich.producer.service;

import com.elyashevich.producer.producer.TransactionalMessageProducerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducerServiceImpl implements MessageProducerService {

    private final TransactionalMessageProducerImpl transactionalMessageProducer;

    @Override
    public void sendMessagesWithTransaction(boolean shouldRollback) {
        String transactionId = shouldRollback ? 
            "rollback-" + System.currentTimeMillis() : 
            "commit-" + System.currentTimeMillis();
        
        log.info("Starting transaction: {}", transactionId);
        
        try {
            transactionalMessageProducer.sendTransactionalMessages(transactionId, 5);
            log.info("Transaction completed successfully: {}", transactionId);
        } catch (Exception e) {
            log.warn("Transaction failed: {}, reason: {} ",transactionId, e.getMessage());
        }
    }
}