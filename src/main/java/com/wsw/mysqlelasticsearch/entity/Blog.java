package com.wsw.mysqlelasticsearch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 6320548148250372657L;

    @Id
    @TableId(type = IdType.AUTO)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    @TableField(value = "title")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "time")
    private Date time;
}
