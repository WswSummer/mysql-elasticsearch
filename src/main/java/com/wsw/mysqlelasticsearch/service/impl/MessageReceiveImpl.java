package com.wsw.mysqlelasticsearch.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.wsw.mysqlelasticsearch.entity.Blog;
import com.wsw.mysqlelasticsearch.service.ElasticService;
import com.wsw.mysqlelasticsearch.service.MessageReceive;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Author WangSongWen
 * @Date: Created in 15:11 2021/1/25
 * @Description: 消息接收主服务
 */
@Service
@Slf4j
public class MessageReceiveImpl implements MessageReceive {
    @Resource
    private ElasticService elasticService;

    @RabbitHandler
    @RabbitListener(queues = "queueBlog")
    public void receiveMessage(Message message, Channel channel, Map<String, Object> messageMap) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            log.info("数据同步服务接收到了消息: " + objectMapper.writeValueAsString(messageMap));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            // 获取数据操作类型
            String operationType = MapUtils.getString(messageMap, "operationType");
            String blogStr;
            Blog blog;
            switch (operationType) {
                case "ADD":
                    blogStr = MapUtils.getString(messageMap, "blog");
                    blog = objectMapper.readValue(blogStr, Blog.class);
                    if (blog != null) {
                        try {
                            elasticService.addBlog(blog);
                            log.info("添加数据同步至ElasticSearch成功!");
                        } catch (Exception e) {
                            log.error("添加数据同步至ElasticSearch失败! " + e.getMessage());;
                        }
                    }
                    break;
                case "DELETE":
                    String blogId = MapUtils.getString(messageMap, "blogId");
                    if (blogId != null) {
                        try {
                            elasticService.deleteBlogById(blogId);
                            log.info("删除数据同步至ElasticSearch成功!");
                        } catch (Exception e) {
                            log.error("删除数据同步至ElasticSearch失败! " + e.getMessage());
                        }
                    }
                    break;
                case "UPDATE":
                    blogStr = MapUtils.getString(messageMap, "blog");
                    blog = objectMapper.readValue(blogStr, Blog.class);
                    if (blog != null) {
                        try {
                            elasticService.updateBlog(blog);
                            log.info("更新数据同步至ElasticSearch成功!");
                        } catch (Exception e) {
                            log.error("更新数据同步至ElasticSearch失败! " + e.getMessage());
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                log.info("消息已重复处理失败,拒绝再次接收");
                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.info("消息即将再次返回队列处理");
                // requeue为是否重新回到队列，true重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
            log.error(e.getMessage());
        }
    }

}
