package org.example.elastic.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobDetail {
    @JsonIgnore
    private long id;
    private String area;
    private String exp;
    private String edu;
    private String salary;
    @JsonProperty("job_type")
    private String jobType;
    private String cmp;
    private long pv;
    private String title;
    private String jd;
    private int age;
}
