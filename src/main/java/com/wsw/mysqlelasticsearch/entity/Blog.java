package com.wsw.mysqlelasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author WangSongWen
 * @Date: Created in 10:46 2021/1/22
 * @Description: 实体类
 */
@Data
@Accessors(chain = true)
@Document(indexName = "blog", type = "_doc")
@Entity
public class Blog implements Serializable {

    private static final long serialVersionUID = 6320548148250372657L;

    @Id
    @javax.persistence.Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date time;
}
