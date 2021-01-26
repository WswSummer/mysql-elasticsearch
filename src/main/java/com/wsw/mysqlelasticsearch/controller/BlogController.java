package com.wsw.mysqlelasticsearch.controller;

import com.wsw.mysqlelasticsearch.api.CommonResult;
import com.wsw.mysqlelasticsearch.entity.Blog;
import com.wsw.mysqlelasticsearch.service.BlogService;
import com.wsw.mysqlelasticsearch.service.ElasticService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author WangSongWen
 * @Date: Created in 15:44 2021/1/22
 * @Description:
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private ElasticService elasticService;

    @Resource
    private BlogService blogService;

    @PostMapping("/add")
    public CommonResult addBlog(@RequestBody Blog blog) {
        try {
            blogService.addBlog(blog);
            return CommonResult.success(blog);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public CommonResult deleteBlogById(@PathVariable String id) {
        if (StringUtils.isBlank(id))
            return CommonResult.failed("id is empty");
        try {
            blogService.deleteBlogById(id);
            return CommonResult.success("删除成功, id = " + id);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/update")
    public CommonResult updateBlog(@RequestBody Blog blog) {
        try {
            blogService.updateBlog(blog);
            return CommonResult.success(blog);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public CommonResult getBlogById(@PathVariable String id) {
        if (StringUtils.isBlank(id))
            return CommonResult.failed("id is empty");
        try {
            Optional<Blog> blogOptional = elasticService.getBlogById(id);
            Blog blog = null;
            if (blogOptional.isPresent()) {
                blog = blogOptional.get();
            }
            return CommonResult.success(blog);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @GetMapping("/get/all")
    public CommonResult getAllBlog() {
        try {
            Iterable<Blog> iterable = elasticService.getAllBlog();
            List<Blog> list = new ArrayList<>();
            iterable.forEach(list::add);
            return CommonResult.success(list);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @GetMapping("/search")
    public CommonResult search(@RequestParam String keyWord) {
        try {
            Page<Blog> blogPage = elasticService.search(keyWord);
            List<Blog> blogs = blogPage.getContent();
            return CommonResult.success(blogs);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }
}
