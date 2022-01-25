package org.example.zookeeper.example.lock;

import org.example.zookeeper.example.lock.entity.Order;
import org.example.zookeeper.example.lock.entity.Product;
import org.example.zookeeper.example.lock.mapper.OrderMapper;
import org.example.zookeeper.example.lock.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class OrderService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;


    @Transactional
    public void reduceStock(Integer id) {
        // 1、获取库存
        Product product = productMapper.getProduct(id);
        // 模拟耗时业务处理
        sleep(500);

        if (product.getStock() <= 0) {
            throw new RuntimeException("out of stock");
        }

        // 2、减库存
        int i = productMapper.deductStock(id, 1);
        if (i == 1) {
            Order order = new Order();
            order.setUId(UUID.randomUUID().toString());
            order.setPId(id);
            orderMapper.insert(order);
        } else {
            throw new RuntimeException("deduct stock fail, retry.");
        }

    }

    /**
     * 模拟耗时业务处理
     */
    public void sleep(long wait) {
        try {
            TimeUnit.MILLISECONDS.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
