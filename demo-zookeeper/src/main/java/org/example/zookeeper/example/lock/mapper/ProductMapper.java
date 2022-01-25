package org.example.zookeeper.example.lock.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.zookeeper.example.lock.entity.Product;

@Mapper
public interface ProductMapper {
    @Select("select * from product where id=#{id}")
    Product getProduct(@Param("id") Integer id);

    @Update("update product set stock=stock-#{num} where id=#{id}")
    int deductStock(@Param("id") Integer id, @Param("num") Integer num);
}
