package com.wsw.mysqlelasticsearch.service;

import com.wsw.mysqlelasticsearch.entity.Blog;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * @Author WangSongWen
 * @Date: Created in 15:45 2021/1/22
 * @Description:
 */
public interface ElasticService {
    Page<Blog> search(String keyWord);

    void addBlog(Blog blog);

    Optional<Blog> getBlogById(Long id);

    Iterable<Blog> getAllBlog();

    void deleteAllBlog();

    void deleteBlogById(Long id);

    void updateBlog(Blog blog);
}
