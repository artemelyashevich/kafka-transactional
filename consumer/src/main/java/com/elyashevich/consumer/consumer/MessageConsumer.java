package com.elyashevich.consumer.consumer;

import com.elyashevich.consumer.excprion.BusinessException;
import com.elyashevich.consumer.model.MessageModel;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private static final int PROCESSING_THREADS = 4;

    private final ExecutorService processingExecutor = Executors.newFixedThreadPool(PROCESSING_THREADS);

    // private static final int MAX_QUEUE_SIZE = 1000;
    // private final Map<String, BlockingQueue<MessageModel>> messageQueues = new ConcurrentHashMap<>();

    @KafkaListener(topics = "transactional-messages", concurrency = "4", groupId = "transactional-messages-group")
    public void consumeMessage(ConsumerRecord<String, MessageModel> messageModel) {
        try {
            log.info("Consuming message: {}", messageModel);
        } catch (Exception e) {
            log.error("Failed to process Message message", e);
        }
    }

//
//    private boolean offerMessageToQueue(String messageId, MessageModel message) {
//        var queue = messageQueues.computeIfAbsent(
//                messageId,
//                k -> new LinkedBlockingQueue<>(MAX_QUEUE_SIZE)
//        );
//        return queue.offer(message);
//    }
//
//    private void processMessages(String productId, String topicName, String MessageId) {
//        try {
//            var queue = messageQueues.get(MessageId);
//            if (queue == null) return;
//
//            MessageModel message;
//            while ((message = queue.poll()) != null) {
//                processSinglemessageWithRetry(productId, topicName, message);
//            }
//        } finally {
//            messageQueues.remove(MessageId);
//        }
//    }
//
//    private void processSinglemessageWithRetry(String productId, String topicName, MessageModel message) {
//        var attempt = 0;
//        while (attempt < 3) {
//            try {
//                processSingleMessage(productId, topicName, message);
//                return;
//            } catch (Exception e) {
//                attempt++;
//                if (attempt >= 3) {
//                    log.error("Failed to process Message {} after {} attempts",
//                            message.getId(), 3);
//                    throw e;
//                }
//                waitBeforeRetry(attempt);
//            }
//        }
//    }
//
//    private void processSingleMessage(String productId, String topicName, MessageModel message) {
//        try {
//            log.debug("Processed Message message: {}", message.getId());
//        } catch (Exception e) {
//            log.error("Error processing Message message: {}", message.getId(), e);
//        }
//    }
//
//    private void waitBeforeRetry(int attempt) {
//        try {
//            var delay = Math.pow(2, attempt) * 100;
//            Thread.sleep(((Double) delay).longValue());
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }

    @PreDestroy
    public void shutdown() {
        processingExecutor.shutdown();
        try {
            if (!processingExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                processingExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            processingExecutor.shutdownNow();
        }
    }
}