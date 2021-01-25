package com.wsw.mysqlelasticsearch.repository.jpa;

import com.wsw.mysqlelasticsearch.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author WangSongWen
 * @Date: Created in 14:50 2021/1/25
 * @Description:
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {
}
