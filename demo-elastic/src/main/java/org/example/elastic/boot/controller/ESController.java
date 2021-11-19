package org.example.elastic.boot.controller;

import org.example.elastic.boot.repository.ESProductRepository;
import org.example.elastic.boot.service.MallSearchService;
import org.example.elastic.boot.vo.ESRequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ESController {

    @Autowired
    private ESProductRepository esProductRepository;

    @Autowired
    private MallSearchService mallSearchService;

    @PostMapping("/listPage")
    public Object listPage(@RequestBody ESRequestParam param) {
        return mallSearchService.search(param);
    }

}
