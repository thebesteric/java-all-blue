package org.example.sharding.jdbc.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shardingsphere.api.hint.HintManager;
import org.example.sharding.jdbc.entity.Course;
import org.example.sharding.jdbc.entity.Dict;
import org.example.sharding.jdbc.entity.User;
import org.example.sharding.jdbc.mapper.CourseMapper;
import org.example.sharding.jdbc.mapper.DictMapper;
import org.example.sharding.jdbc.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingJDBCTest {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    DictMapper dictMapper;

    @Autowired
    UserMapper userMapper;

    @Test
    public void addCourse() {
        for (int i = 0; i < 10; i++) {
            Course course = new Course();
            // course.setCid((long) i);
            course.setCname("ShardingJDBC-" + i);
            course.setUserId((long) (1000 + i));
            course.setCstatus("1");
            courseMapper.insert(course);
        }
    }

    // inline 策略
    // application-01.properties
    @Test
    public void findCourse() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("cid");
        // queryWrapper.orderByAsc("cname");
        // queryWrapper.eq("cid", 672089321100021761L);

        List<Course> courses = courseMapper.selectList(queryWrapper);

        courses.forEach(System.out::println);
    }

    // standard 策略：支持 between
    // application-02.properties
    @Test
    public void findCourseRange() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("cid", 672089320634454016L, 672089321024524288L);
        queryWrapper.orderByAsc("cid");

        List<Course> courses = courseMapper.selectList(queryWrapper);

        courses.forEach(System.out::println);
    }

    // complex 策略
    // application-03.properties
    @Test
    public void findCourseComplex() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("cid", 672089320634454016L, 672089321024524288L);
        queryWrapper.eq("user_id", 1000L);

        List<Course> courses = courseMapper.selectList(queryWrapper);

        courses.forEach(System.out::println);
    }

    // hint 策略：指定要查询的分片
    // application-04.properties
    @Test
    public void findCourseHint() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.addTableShardingValue("course", 2);
        List<Course> courses = courseMapper.selectList(null);

        courses.forEach(System.out::println);
    }

    // 广播表：分库的时候，同步相同的表
    // application-05.properties
    @Test
    public void broadcastDict() {
        Dict dict1 = new Dict();
        dict1.setUstatus("1");
        dict1.setUvalue("正常");
        dictMapper.insert(dict1);

        Dict dict2 = new Dict();
        dict2.setUstatus("0");
        dict2.setUvalue("异常");
        dictMapper.insert(dict2);
    }

    // 绑定表：解决由于分表，使用 join 的时候出现笛卡尔集的问题
    // application-06.properties
    @Test
    public void bindTable() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("username_" + i);
            user.setUstatus((i % 2) + "");
            user.setUage(i + 10);
            // userMapper.insert(user);
        }

        System.out.println("insert succeed");

        userMapper.queryUserStatus().forEach(System.out::println);
    }

}
