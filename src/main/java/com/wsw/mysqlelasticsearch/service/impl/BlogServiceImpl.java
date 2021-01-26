package com.wsw.mysqlelasticsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsw.mysqlelasticsearch.api.OperationType;
import com.wsw.mysqlelasticsearch.entity.Blog;
import com.wsw.mysqlelasticsearch.repository.jpa.BlogRepository;
import com.wsw.mysqlelasticsearch.service.BlogService;
import com.wsw.mysqlelasticsearch.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author WangSongWen
 * @Date: Created in 14:50 2021/1/25
 * @Description:
 */
@Service
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
            e.printStackTrace();
        }
        Map<String, Object> message = new HashMap<>();
        message.put("operationType", OperationType.ADD.getOperation());
        message.put("blog", blogStr);
        messageService.sendMessage(message);
    }
}
