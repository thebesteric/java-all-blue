package com.example.demo.convert;


import com.example.demo.dto.CarDTO;
import com.example.demo.dto.DriverDTO;
import com.example.demo.dto.PartDTO;
import com.example.demo.vo.CarVO;
import com.example.demo.vo.DriverVO;
import com.example.demo.vo.VehicleVO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Car 相关的 pojo 转换
 * 1. 引入依赖
 * 2. 新建一个抽象类或者接口，并标记为 @Mapper
 * 3. 写一个转换方法，方法名可以任意
 * 4. 获取对象并使用：public static CarConvert INSTANCE = Mappers.getMapper(CarConvert.class);
 * 5. 如果要和 Spring 整合，则指定：@Mapper(componentModel = "spring")
 * @author wangweijun
 * @version v1.0
 * @since 2023-10-31 18:49:14
 */
@Mapper(componentModel = "spring")
// @Mapper
public abstract class CarConvert {

    public static CarConvert INSTANCE = Mappers.getMapper(CarConvert.class);

    /**
     * CarDTO -> CarVO
     */
    @Mappings({
            @Mapping(source = "totalPrice", target = "totalPrice", numberFormat = "#.00"),
            @Mapping(source = "publishDate", target = "publishDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "color", ignore = true),
            @Mapping(source = "brand", target = "brandName"),
            @Mapping(source = "driverDTO", target = "driverVO"),
    })
    public abstract CarVO toCarVO(CarDTO carDTO);

    /**
     * DriverDTO -> DriverVO
     */
    @Mapping(source = "id", target = "driverId")
    @Mapping(source = "name", target = "driverName")
    public abstract DriverVO toDriverVO(DriverDTO driverDTO);


    /**
     * @AfterMapping： 表示 mapstruct 在转换完之后，会自动调用该方法
     * @MappingTarget：表示传入的 VO 是进行转换完成（赋值过）的
     */
    @AfterMapping
    public void dto2voAfter(CarDTO carDTO, @MappingTarget CarVO carVO) {
        List<PartDTO> partsDTOs = carDTO.getPartDTOs();
        boolean hasParts = partsDTOs != null && !partsDTOs.isEmpty();
        carVO.setHasParts(hasParts);
    }

    /**
     * 批量转换
     * List<CarDTO> -> List<CarVO>
     */
    public abstract List<CarVO> toCarVOs(List<CarDTO> carDTOs);


    /**
     * 使用 @BeanMapping 配置忽略 MapStruct 的默认映射行为，只映射哪些配置了 @Mapping 的属性
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "brand", target = "brandName")
    public abstract VehicleVO toVehicleVO(CarDTO carDTO);

    /**
     * 继承映射 @InheritConfiguration：更新场景使用，避免同样的配置，在重复写多份
     */
    @InheritConfiguration
    @Mapping(target = "id", ignore = true)
    public abstract void updateVehicleVO(CarDTO carDTO, @MappingTarget VehicleVO vehicleVO);

    /**
     * 反向继承映射 @InheritInverseConfiguration
     * name：指定使用哪一个方法的配置
     */
    @InheritInverseConfiguration(name = "toVehicleVO")
    public abstract CarDTO toCarDTO(VehicleVO vehicleVO);
}
