package com.elyashevich.producer.producer;

public interface TransactionalMessageProducer {

    void sendTransactionalMessages(String transactionId, int count);
}
