package com.elyashevich.producer.controller;

import com.elyashevich.producer.service.MessageProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageProducerService messageProducerService;

    @PostMapping
    public void sendMessage(@RequestBody String isRollback) {
        this.messageProducerService.sendMessagesWithTransaction(Boolean.parseBoolean(isRollback));
    }
}
