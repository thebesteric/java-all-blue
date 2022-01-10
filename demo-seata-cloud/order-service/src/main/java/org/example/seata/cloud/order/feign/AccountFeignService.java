package org.example.seata.cloud.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Fox
 */
@FeignClient(name = "account-service",path = "/account")
public interface AccountFeignService {
    
    @RequestMapping("/debit")
    Boolean debit(@RequestParam("userId") String userId,@RequestParam("money") int money);
}
