package com.wsw.mysqlelasticsearch.service.impl;

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
        Map<String, Object> message = new HashMap<>();
        message.put("operationType", OperationType.ADD.getOperation());
        message.put("blog", blog);
        messageService.sendMessage(message);
    }
}
