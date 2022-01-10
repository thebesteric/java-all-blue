package org.example.seata.cloud.order.service;

import io.seata.core.exception.TransactionException;
import org.example.seata.cloud.datasource.entity.Order;
import org.example.seata.cloud.order.vo.OrderVo;

public interface OrderService {

    /**
     * 保存订单
     */
    Order saveOrder(OrderVo orderVo) throws TransactionException;
}