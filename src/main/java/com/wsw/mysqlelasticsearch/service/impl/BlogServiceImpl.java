package com.wsw.mysqlelasticsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsw.mysqlelasticsearch.api.OperationType;
import com.wsw.mysqlelasticsearch.entity.Blog;
import com.wsw.mysqlelasticsearch.mapper.BlogMapper;
import com.wsw.mysqlelasticsearch.service.BlogService;
import com.wsw.mysqlelasticsearch.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author WangSongWen
 * @Date: Created in 14:50 2021/1/25
 * @Description: Blog主服务
 */
@Service
@Slf4j
public class BlogServiceImpl implements BlogService {
    @Resource
    private BlogMapper blogMapper;

    @Resource
    private MessageService messageService;

    @Resource
    private RedissonClient redissonClient;

    private static final String REDIS_LOCK_KEY = "blog-service";

    @Override
    public void addBlog(Blog blog) {
        blogMapper.insert(blog);
        ObjectMapper objectMapper = new ObjectMapper();
        String blogStr = null;
        try {
            blogStr = objectMapper.writeValueAsString(blog);
        } catch (JsonProcessingException e) {
            log.error("json转换失败: " + e.getMessage());
        }
        Map<String, Object> message = new HashMap<>();
        message.put("operationType", OperationType.ADD.getOperation());
        message.put("blog", blogStr);
        try {
            messageService.sendMessage(message);
        } catch (Exception e) {
            log.error("发送消息到队列失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteBlogById(String id) {
        RLock lock = redissonClient.getLock(REDIS_LOCK_KEY);
        lock.lock(30, TimeUnit.SECONDS);
        try {
            blogMapper.deleteById(id);
        } catch (Exception e) {
            log.error("删除失败: " + e.getMessage());
        } finally {
            lock.unlock();
        }
        Map<String, Object> message = new HashMap<>();
        message.put("operationType", OperationType.DELETE.getOperation());
        message.put("blogId", id);
        try {
            messageService.sendMessage(message);
        } catch (Exception e) {
            log.error("发送消息到队列失败: " + e.getMessage());
        }
    }

    @Override
    public void updateBlog(Blog blog) {
        blogMapper.updateById(blog);
        ObjectMapper objectMapper = new ObjectMapper();
        String blogStr = null;
        try {
            blogStr = objectMapper.writeValueAsString(blog);
        } catch (JsonProcessingException e) {
            log.error("json转换失败: " + e.getMessage());
        }
        Map<String, Object> message = new HashMap<>();
        message.put("operationType", OperationType.UPDATE.getOperation());
        message.put("blog", blogStr);
        try {
            messageService.sendMessage(message);
        } catch (Exception e) {
            log.error("发送消息到队列失败: " + e.getMessage());
        }
    }


}
