package org.example.elastic.boot.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "product_db", type = "_doc", shards = 1, replicas = 1)
public class ESProduct implements Serializable {
    @Id
    private long id;
    @Field(type = FieldType.Keyword)
    private String productSn;
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String name;
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String keywords;
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String subTitle;
    private Double price;
    private Double promotionPrice;
    private Double originalPrice;
    private String pic;
    private long salecount;
    private long sale;
    private boolean hasStock;
    private long brandId;
    @Field(type = FieldType.Keyword)
    private String brandName;
    private String brandImg;
    private long categoryId;
    @Field(type = FieldType.Keyword)
    private String categoryName;
    private Date putawayDate;
    private List<Attr> attrs = new ArrayList<>();

    @Data
    public static class Attr {
        private long attrId;
        private String attrName;
        private String attrValue;
    }

}
