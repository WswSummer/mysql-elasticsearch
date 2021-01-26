package com.wsw.mysqlelasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MysqlElasticsearchApplication {

    public static void main(String[] args) {
        // elasticSearch和redis共用netty连接，导致连接冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(MysqlElasticsearchApplication.class, args);
    }

}
