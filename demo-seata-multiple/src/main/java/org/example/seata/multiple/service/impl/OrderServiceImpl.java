package org.example.seata.multiple.service.impl;


import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.example.seata.multiple.config.DataSourceKey;
import org.example.seata.multiple.config.DynamicDataSourceContextHolder;
import org.example.seata.multiple.entity.Order;
import org.example.seata.multiple.entity.OrderStatus;
import org.example.seata.multiple.mapper.OrderMapper;
import org.example.seata.multiple.service.AccountService;
import org.example.seata.multiple.service.OrderService;
import org.example.seata.multiple.service.StorageService;
import org.example.seata.multiple.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Fox
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private StorageService storageService;

    @Override
    // @Transactional
    @GlobalTransactional(name = "createOrder")
    public Order saveOrder(OrderVo orderVo) {
        log.info("=============用户下单=================");
        //切换数据源
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.ORDER);
        log.info("当前 XID: {}", RootContext.getXID());

        // 保存订单
        Order order = new Order();
        order.setUserId(orderVo.getUserId());
        order.setCommodityCode(orderVo.getCommodityCode());
        order.setCount(orderVo.getCount());
        order.setMoney(orderVo.getMoney());
        order.setStatus(OrderStatus.INIT.getValue());

        int saveOrderRecord = orderMapper.insert(order);
        log.info("保存订单 {}", saveOrderRecord > 0 ? "成功" : "失败");

        // 扣减库存
        storageService.deduct(orderVo.getCommodityCode(), orderVo.getCount());

        // 扣减余额
        accountService.debit(orderVo.getUserId(), orderVo.getMoney());

        log.info("=============更新订单状态=================");
        // 切换数据源
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.ORDER);
        // 更新订单
        int updateOrderRecord = orderMapper.updateOrderStatus(order.getId(), OrderStatus.SUCCESS.getValue());
        log.info("更新订单id: {} {}", order.getId(), updateOrderRecord > 0 ? "成功" : "失败");

        return order;

    }
}
