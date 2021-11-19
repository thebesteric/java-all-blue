package org.example.elastic.boot.service.impl;

import org.elasticsearch.client.RestHighLevelClient;
import org.example.elastic.boot.entity.ESProduct;
import org.example.elastic.boot.repository.ESProductRepository;
import org.example.elastic.boot.service.SpringDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 商品搜索管理Service实现类
 */
@Service
public class SpringDataServiceImpl implements SpringDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataServiceImpl.class);

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ESProductRepository productRepository;


    @Override
    public ESProduct create(Long id) {
        ESProduct esProduct = new ESProduct();
        esProduct.setId(id);
        esProduct.setName("测试手机-" + id);
        esProduct.setProductSn(System.currentTimeMillis() + "");
        esProduct.setBrandId(1);
        esProduct.setBrandName("测试");

        productRepository.save(esProduct);
        return null;
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);

    }

    @Override
    public void delete(List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            List<ESProduct> esProductList = new ArrayList<>();
            for (Long id : ids) {
                ESProduct esProduct = new ESProduct();
                esProduct.setId(id);
                esProductList.add(esProduct);
            }
            productRepository.deleteAll(esProductList);
        }

    }

    @Override
    public Page<ESProduct> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return productRepository.findByNameOrSubTitleOrKeywords(keyword, keyword, keyword, pageable);
    }

    @Override
    public ESProduct findById(long id) {
        return productRepository.findById(id).orElse(null);
    }


}
