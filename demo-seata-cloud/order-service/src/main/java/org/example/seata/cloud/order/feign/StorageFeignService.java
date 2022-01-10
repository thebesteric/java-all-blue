package org.example.seata.cloud.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Fox
 */
@FeignClient(name="storage-service",path="/storage")
public interface StorageFeignService {
    
    @RequestMapping(path = "/deduct")
    public Boolean deduct(@RequestParam("commodityCode") String commodityCode,@RequestParam("count") Integer count);
    
}
