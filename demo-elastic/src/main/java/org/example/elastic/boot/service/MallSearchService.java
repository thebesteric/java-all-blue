package org.example.elastic.boot.service;

import org.example.elastic.boot.vo.ESRequestParam;
import org.example.elastic.boot.vo.ESResponseResult;


/**
 * @author 白起老师
 */
public interface MallSearchService {


    /**
     * @param param 检索的所有参数
     * @return  返回检索的结果，里面包含页面需要的所有信息
     */
    ESResponseResult search(ESRequestParam param);


}


