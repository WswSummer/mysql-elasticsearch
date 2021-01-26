package com.wsw.mysqlelasticsearch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsw.mysqlelasticsearch.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WangSongWen
 * @Date: Created in 14:27 2021/1/26
 * @Description:
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
