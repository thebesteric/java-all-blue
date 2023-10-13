package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.example.springboot.entity.Test;
import org.example.springboot.mapper.TestMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TestService
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-18 18:15:27
 */
@Service
@RequiredArgsConstructor
public class TestService {

    private final TestMapper testMapper;

    public List<Test> selectList() {
        QueryWrapper<Test> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("1", "1");
        return testMapper.selectList(queryWrapper);
    }

    public Page<Test> selectPage(int pageNum, int pageSize) {
        Page<Test> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        QueryWrapper<Test> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("1", "1");
        return testMapper.selectPage(page, queryWrapper);
    }

}
