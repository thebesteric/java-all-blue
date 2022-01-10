package org.example.seata.multiple.service;


import org.example.seata.multiple.entity.Order;
import org.example.seata.multiple.vo.OrderVo;

public interface OrderService {

    /**
     * 保存订单
     */
    Order saveOrder(OrderVo orderVo);
}