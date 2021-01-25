package com.wsw.mysqlelasticsearch.service;

import java.util.Map;

/**
 * @Author WangSongWen
 * @Date: Created in 14:58 2021/1/25
 * @Description:
 */
public interface MessageService {
    void sendMessage(Map<String, Object> message);
}
