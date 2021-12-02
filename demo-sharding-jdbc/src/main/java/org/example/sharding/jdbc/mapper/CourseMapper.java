package org.example.sharding.jdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.sharding.jdbc.entity.Course;
import org.springframework.stereotype.Component;

@Component
public interface CourseMapper extends BaseMapper<Course> {
}
