package com.yong.user.facade.service;

import com.yong.service.commons.core.utils.JacksonUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    public static final String CACHE_KEY_USER = "user";

    private final RedisTemplate<String, String> redisTemplate;

    public TokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void save(String token, String dataName, Object data) {
        String cacheKey = getCacheKey(token);
        redisTemplate.opsForHash().put(cacheKey, dataName, JacksonUtils.toJson(data));
        redisTemplate.expire(cacheKey, 30, TimeUnit.MINUTES);
    }

    public <T> T getData(String token, Class<T> clazz) {
        String cacheKey = getCacheKey(token);
        Object userObj = redisTemplate.opsForHash().get(cacheKey, CACHE_KEY_USER);
        if(userObj == null) {
            return null;
        }
        return JacksonUtils.parse(userObj.toString(), clazz);
    }

    public boolean hasToken(String token) {
        String cacheKey = getCacheKey(token);
        Boolean hasKey = redisTemplate.hasKey(cacheKey);
        if(hasKey == null) {
            return false;
        }
        if(hasKey) {
            redisTemplate.expire(cacheKey, 30, TimeUnit.MINUTES);
        }
        return hasKey;
    }

    private String getCacheKey(String token) {
        return "tk::" + token;
    }
}
