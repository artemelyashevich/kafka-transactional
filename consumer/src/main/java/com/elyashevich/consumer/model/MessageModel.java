package com.elyashevich.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {

    private String id;

    private String content;

    private int sequence;
}
