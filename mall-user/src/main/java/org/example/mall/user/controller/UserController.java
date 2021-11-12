package org.example.mall.user.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.example.mall.comm.R;
import org.example.mall.user.sentinel.CommonBlockHandler;
import org.example.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${server.port}")
    private int port;

    @Autowired
    private UserService userService;

    @GetMapping("/get/{id}")
    public R get(@PathVariable String id,
                     @RequestHeader(value = "X-Request-color", required = false) String xRequestColor,
                    @RequestHeader(value = "x-user-id", required = false) String userId,
                     @RequestParam(value = "color", required = false) String color) throws UnknownHostException {
        System.out.println("request-header=" + xRequestColor);
        System.out.println("request-param=" + color);
        System.out.println("x-user-id=" + userId);
        userService.get(id);
        return R.success().setData("[" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "] user-" + id);
    }

    // controller 限流
    @SentinelResource(value = "test",
            blockHandlerClass = CommonBlockHandler.class,
            blockHandler = "handleException2")
    @GetMapping("/test/{id}")
    public R test(@PathVariable String id) throws UnknownHostException {
        return R.success().setData("[" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "] test-" + id);
    }

    // service 限流
    @GetMapping("/getUser/{id}")
    public R getUser(@PathVariable String id) {
        return R.success().setData(userService.getById(id));
    }

    private static final String RESOURCE_NAME = "list";

    @GetMapping("/list")
    public R list() {
        Entry entry = null;
        try {
            // 资源名可使用任意有业务语义的字符串
            entry = SphU.entry(RESOURCE_NAME);
            // 被保护的业务逻辑
            return R.success();
        } catch (BlockException e) {
            // 资源访问阻止，被限流或被降级
            e.printStackTrace();
            return R.error("限流了");
        } catch (Exception e) {
            // 资源访问阻止，被限流或被降级
            e.printStackTrace();
            // 若需要配置降级规则，需要通过这种方式记录业务异常
            Tracer.traceEntry(e, entry);
            return R.error("限流了");
        } finally {
            // 务必保证 exit，务必保证每个 entry 与 exit 配对
            if (entry != null) {
                entry.exit();
            }
        }
    }

    @PostConstruct
    private static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        //设置受保护的资源
        rule.setResource(RESOURCE_NAME);
        // 设置流控规则 QPS
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 设置受保护的资源阈值
        // Set limit QPS to 20.
        rule.setCount(1);
        rules.add(rule);
        // 加载配置好的规则
        FlowRuleManager.loadRules(rules);
    }
}
