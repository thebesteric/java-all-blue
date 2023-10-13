package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.Test;

import java.util.List;

@Mapper
public interface TestMapper extends BaseMapper<Test> {

    @Select("select * from test where 1 = 1")
    List<Test> select();

    // @IgnoreMethod
    @Delete("select * from test where name = #{name}")
    int deleteByName(@Param("name") String name);


}
