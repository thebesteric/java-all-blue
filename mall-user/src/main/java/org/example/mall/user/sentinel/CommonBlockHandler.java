package org.example.mall.user.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.example.mall.comm.R;

import java.util.Map;

public class CommonBlockHandler {

    // 必须为 static 函数，方法名不能相同

    public static R handleException1(Map<String, Object> params, BlockException exception) {
        return R.error("==被限流了==" + exception).setData(params);
    }

    public static R handleException2(String id, BlockException exception) {
        return R.error("==被限流了==" + exception).setData(id);
    }

    public static R handleException3(BlockException exception) {
        return R.error("==被限流了==" + exception);
    }

}
