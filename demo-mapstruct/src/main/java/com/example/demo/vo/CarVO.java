package com.example.demo.vo;

import lombok.Data;

/**
 * CarDTO
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-10-31 17:16:06
 */
@Data
public class CarVO {
    private Long id;
    // 车架号
    private String vin;
    // 裸车价格
    private Double price;
    // 整车上路价格
    private String totalPrice;
    // 颜色
    private String color;
    // 发布日期
    private String publishDate;
    // 品牌
    private String brandName;
    // 是否加配
    private Boolean hasParts;
    // 司机
    private DriverVO driverVO;
}
