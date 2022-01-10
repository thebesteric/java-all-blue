package org.example.seata.cloud.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.seata.cloud.datasource.entity.Order;
import org.example.seata.cloud.datasource.vo.ResultVo;
import org.example.seata.cloud.order.service.OrderService;
import org.example.seata.cloud.order.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fox
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping("/createOrder")
    public ResultVo createOrder(@RequestBody OrderVo orderVo) throws Exception {
        log.info("收到下单请求,用户:{}, 商品编号:{}", orderVo.getUserId(), orderVo.getCommodityCode());
        Order order = orderService.saveOrder(orderVo);
        return ResultVo.ok().put("order",order);
    }
    
}
