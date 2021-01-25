package com.wsw.mysqlelasticsearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author WangSongWen
 * @Date: Created in 16:58 2021/1/25
 * @Description: spring-boot-starter-data-elasticsearch 与 spring-boot-starter-data-jpa一起用报错 bean重复
 * 指定各自的repository包
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.wsw.mysqlelasticsearch.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.wsw.mysqlelasticsearch.repository.es")
public class RepositoryConfig {

}
