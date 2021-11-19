package org.example.elastic.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.elastic.boot.entity.ESProduct;
import org.example.elastic.boot.service.SpringDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搜索商品管理Controller
 */
@Controller
@Api(tags = "EsProductController", description = "搜索商品管理")
@RequestMapping("/esProduct")
public class SpringDataController {

    @Autowired
    private SpringDataService springDataService;

    @ApiOperation(value = "根据id查找商品")
    @GetMapping("/getById/{id}")
    public Object getById(@PathVariable("id") long id) {
        return springDataService.findById(id);
    }


    @ApiOperation(value = "根据id创建商品")
    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ESProduct create(@PathVariable Long id) {
        return springDataService.create(id);
    }

    @ApiOperation(value = "根据id删除商品")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id) {
        springDataService.delete(id);
        return "succeed";
    }

    @ApiOperation(value = "根据id批量删除商品")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@RequestParam("ids") List<Long> ids) {
        springDataService.delete(ids);
        return "succeed";
    }

    @ApiOperation(value = "简单搜索")
    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
    @ResponseBody
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<ESProduct> esProductPage = springDataService.search(keyword, pageNum, pageSize);
        System.out.println("esProductPage.toString():" + esProductPage.toString() + " esProductPage.getContent():" + esProductPage.getContent().size());
        return esProductPage.getContent().toString();
    }

}
