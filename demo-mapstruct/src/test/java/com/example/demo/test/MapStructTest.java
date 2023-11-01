package com.example.demo.test;

import com.example.demo.MapStructApplication;
import com.example.demo.convert.CarConvert;
import com.example.demo.dto.CarDTO;
import com.example.demo.dto.DriverDTO;
import com.example.demo.dto.PartDTO;
import com.example.demo.vo.CarVO;
import com.example.demo.vo.DriverVO;
import com.example.demo.vo.VehicleVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MapStructTest
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-10-31 17:34:28
 */
@SpringBootTest(classes = MapStructApplication.class)
public class MapStructTest {

    @Autowired
    private CarConvert carConvert;

    /**
     * 测试 spring 注入，需要使用 @Mapper(componentModel = "spring")
     */
    @Test
    void test7() {
        System.out.println(carConvert);
    }


    /**
     * 反向继承映射 @InheritInverseConfiguration
     */
    @Test
    void test6() {
        VehicleVO vehicleVO = new VehicleVO();
        vehicleVO.setId(999L);
        vehicleVO.setBrandName("宝马");
        vehicleVO.setPrice(555555.555);
        CarDTO carDTO = CarConvert.INSTANCE.toCarDTO(vehicleVO);
        System.out.println(carDTO);
    }

    /**
     * 继承映射 @InheritConfiguration：更新场景使用，避免同样的配置，在重复写多份
     */
    @Test
    void test5() {
        CarDTO carDTO = buildCartDTO();
        VehicleVO vehicleVO = CarConvert.INSTANCE.toVehicleVO(carDTO);
        System.out.println(vehicleVO);

        CarDTO carDTO2 = new CarDTO();
        carDTO2.setBrand("奥迪");
        CarConvert.INSTANCE.updateVehicleVO(carDTO2, vehicleVO);
        System.out.println(vehicleVO);
    }

    /**
     * 测试 @BeanMapping 指定属性转换
     */
    @Test
    void test4() {
        CarDTO carDTO = buildCartDTO();
        VehicleVO vehicleVO = CarConvert.INSTANCE.toVehicleVO(carDTO);
        System.out.println(vehicleVO);
    }

    /**
     * 批量转换：List<CarDTO> -> List<CarVO>
     */
    @Test
    void test3() {
        CarDTO carDTO = buildCartDTO();
        List<CarDTO> carDTOs = List.of(carDTO);
        List<CarVO> carVOs = CarConvert.INSTANCE.toCarVOs(carDTOs);
        System.out.println(carVOs);
    }

    /**
     * MapStruct：CarDTO -> CarVO
     */
    @Test
    void test2() {
        CarDTO carDTO = buildCartDTO();
        CarVO carVO = CarConvert.INSTANCE.toCarVO(carDTO);
        System.out.println(carVO);
    }


    /**
     * 普通方法：CarDTO -> CarVO
     */
    @Test
    public void test1() {
        CarDTO carDTO = buildCartDTO();

        CarVO carVO = new CarVO();
        carVO.setId(carDTO.getId());
        carVO.setVin(carDTO.getVin());
        carVO.setPrice(carDTO.getPrice()); // 自动拆箱装箱

        double totalPrice = carDTO.getTotalPrice();
        DecimalFormat df = new DecimalFormat("#.00");
        String totalPriceStr = df.format(totalPrice);
        carVO.setTotalPrice(totalPriceStr);

        Date publishDate = carDTO.getPublishDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String publishDateStr = sdf.format(publishDate);
        carVO.setPublishDate(publishDateStr);

        carVO.setBrandName(carDTO.getBrand());

        List<PartDTO> partsDTOs = carDTO.getPartDTOs();
        boolean hasParts = CollectionUtils.isEmpty(partsDTOs);
        carVO.setHasParts(hasParts);

        DriverVO driverVO = new DriverVO();
        DriverDTO driverDTO = carDTO.getDriverDTO();
        driverVO.setDriverId(driverDTO.getId());
        driverVO.setDriverName(driverDTO.getName());
        carVO.setDriverVO(driverVO);

        System.out.println(carVO);
    }

    private CarDTO buildCartDTO() {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(333L);
        carDTO.setVin("vin123456789");
        carDTO.setPrice(123456.126d);
        carDTO.setTotalPrice(143456.126d);
        carDTO.setPublishDate(new Date());
        carDTO.setBrand("大众");

        PartDTO partDTO1 = new PartDTO();
        partDTO1.setId(10001L);
        partDTO1.setName("多功能方向盘");

        PartDTO partDTO2 = new PartDTO();
        partDTO2.setId(100002L);
        partDTO2.setName("全景天窗");

        List<PartDTO> partDTOs = new ArrayList<>();
        partDTOs.add(partDTO1);
        partDTOs.add(partDTO2);

        carDTO.setPartDTOs(partDTOs);

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setId(88L);
        driverDTO.setName("小明");

        carDTO.setDriverDTO(driverDTO);

        return carDTO;

    }


}
