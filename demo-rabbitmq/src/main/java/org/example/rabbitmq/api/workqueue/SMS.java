package org.example.rabbitmq.api.workqueue;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SMS {
    private String name;
    private String mobile;
    private String content;
}
