package com.elyashevich.producer.service;

import com.elyashevich.producer.exception.BusinessException;
import com.elyashevich.producer.producer.TransactionalMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.TransactionAbortedException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducerServiceImpl implements MessageProducerService {

    private final TransactionalMessageProducer transactionalMessageProducer;

    @Override
    public void sendMessagesWithTransaction(boolean shouldRollback) {
        log.info(Boolean.toString(shouldRollback));
        log.info(shouldRollback ? "Rolling back transaction" : "Committing transaction");
        String transactionId = shouldRollback ?
                "rollback-" + System.currentTimeMillis() :
                "commit-" + System.currentTimeMillis();

        log.info("Starting transaction: {}", transactionId);

        try {
            transactionalMessageProducer.sendTransactionalMessages(transactionId, 5);
            log.info("Transaction completed successfully: {}", transactionId);
        } catch (Exception e) {
            log.warn("Transaction failed: {}, reason: {} ", transactionId, e.getMessage());
            throw new BusinessException(e);
        }
    }
}