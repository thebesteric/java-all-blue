package org.example.zookeeper.example.lock.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.example.zookeeper.example.lock.entity.Order;

@Mapper
public interface OrderMapper {
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert("insert into order(u_id, p_id) values(#{userId}, #{productId})")
    int insert(Order order);
}
