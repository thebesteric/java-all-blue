package com.example.demo.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * CarDTO
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-10-31 17:16:06
 */
@Data
public class CarDTO {
    private Long id;
    // 车架号
    private String vin;
    // 裸车价格
    private double price;
    // 整车上路价格
    private double totalPrice;
    // 颜色
    private String color;
    // 发布日期
    private Date publishDate;
    // 品牌
    private String brand;
    // 加配信息
    private List<PartDTO> partDTOs;
    // 司机
    private DriverDTO driverDTO;
}
