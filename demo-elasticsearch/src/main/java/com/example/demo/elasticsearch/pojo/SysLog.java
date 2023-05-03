package com.example.demo.elasticsearch.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "test_log", shards = 1, replicas = 0)
public class SysLog {
    @Id
    private String id;
    @Field(name = "message", type = FieldType.Text)
    private String message;
    @Field(name = "port", type = FieldType.Integer)
    private Integer port;
    @Field(name = "@version", type = FieldType.Long)
    private Long version;
    @Field(name = "@timestamp", type = FieldType.Date, format = DateFormat.date_time)
    private Date timestamp;
    @Field(name = "host", type = FieldType.Keyword)
    private String host;

    private MessagePojo messagePojo;


    @Data
    public static class MessagePojo {
        @JsonProperty("@timestamp")
        private Date timestamp;
        @JsonProperty("@version")
        private String version;
        private String message;
        private String logger_name;
        private String thread_name;
        private String level;
        private Long level_value;
    }

}
