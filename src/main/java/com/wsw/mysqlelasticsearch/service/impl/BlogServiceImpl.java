package com.wsw.mysqlelasticsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsw.mysqlelasticsearch.api.OperationType;
import com.wsw.mysqlelasticsearch.entity.Blog;
import com.wsw.mysqlelasticsearch.repository.jpa.BlogRepository;
import com.wsw.mysqlelasticsearch.service.BlogService;
import com.wsw.mysqlelasticsearch.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author WangSongWen
 * @Date: Created in 14:50 2021/1/25
 * @Description: Blog主服务
 */
@Service
@Slf4j
public class BlogServiceImpl implements BlogService {
    @Resource
    private BlogRepository blogRepository;

    @Resource
    private MessageService messageService;

    @Override
    public void addBlog(Blog blog) {
        blogRepository.save(blog);
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
        blogRepository.deleteById(id);
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
        blogRepository.saveAndFlush(blog);
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
