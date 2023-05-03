package com.example.demo.elasticsearch.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.IndexOperations;

@Data
@NoArgsConstructor
@Document(indexName = "test_item_index", shards = 1, replicas = 0)
public class Item {
    // 实体类中，可以起名 id 或 _id 与 elasticsearch 中的 _id 匹配
    @Id
    private String id;
    @Field(name = "title", type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    @Field(name = "price", type = FieldType.Long)
    private Long price;
    @Field(name = "category", type = FieldType.Keyword)
    private String category;

    public Item(String id) {
        this(id, null, null, null);
    }

    public Item(String id, String title, Long price, String category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.category = category;
    }

    public Item(String title, Long price, String category) {
        this(null, title, price, category);
    }
}
