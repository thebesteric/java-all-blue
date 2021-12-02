package org.example.sharding.jdbc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_dict")
public class Dict {
    private Long dictId;
    private String ustatus;
    private String uvalue;
}
