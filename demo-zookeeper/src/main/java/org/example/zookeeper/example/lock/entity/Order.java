package org.example.zookeeper.example.lock.entity;

import lombok.Data;

@Data
public class Order {
    private Integer id;
    private Integer pId;
    private String uId;
}
