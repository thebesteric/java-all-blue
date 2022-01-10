package org.example.seata.multiple.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.seata.multiple.entity.Order;
import org.example.seata.multiple.service.OrderService;
import org.example.seata.multiple.vo.OrderVo;
import org.example.seata.multiple.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    public ResultVo createOrder(@RequestBody OrderVo orderVo) throws Exception {
        log.info("收到下单请求, 用户: {}, 商品编号: {}", orderVo.getUserId(), orderVo.getCommodityCode());
        Order order = orderService.saveOrder(orderVo);
        return ResultVo.ok().put("order", order);
    }

}
