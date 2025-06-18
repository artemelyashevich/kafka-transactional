package com.elyashevich.producer.service;

public interface MessageProducerService {

    void sendMessagesWithTransaction(boolean shouldRollback);
}
