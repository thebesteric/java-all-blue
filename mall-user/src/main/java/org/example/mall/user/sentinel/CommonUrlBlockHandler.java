package org.example.mall.user.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mall.comm.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CommonUrlBlockHandler implements UrlBlockHandler {
    @Override
    public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
        log.info("CommonUrlBlockHandler ==== " + e.getRule());
        R r = null;
        if (e instanceof FlowException) {
            r = R.error(100, "接口被限流了");
        } else if (e instanceof DegradeException) {
            r = R.error(100, "接口被降级了");
        } else if (e instanceof ParamFlowException) {
            r = R.error(100, "热点参数被降级了");
        } else if (e instanceof SystemBlockException) {
            r = R.error(100, "触发系统保护规则");
        } else if (e instanceof AuthorityException) {
            r = R.error(100, "触发系统保护规则");
        }

        response.setStatus(500);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), r);
    }
}
