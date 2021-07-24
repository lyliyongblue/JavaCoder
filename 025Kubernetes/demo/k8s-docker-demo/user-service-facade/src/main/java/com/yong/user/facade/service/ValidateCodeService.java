package com.yong.user.facade.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeService {

    private final RedisTemplate<String, String> redisTemplate;

    public ValidateCodeService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean checkCode(String mobile, String code) {
        String cacheKey = getCacheKey(mobile);
        String cacheCode = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isBlank(cacheCode)) {
            return false;
        }
        return cacheCode.equals(code);
    }

    public void removeCode(String mobile) {
        String cacheKey = getCacheKey(mobile);
        redisTemplate.delete(cacheKey);
    }

    public void saveCode(String mobile, String code) {
        String cacheKey = getCacheKey(mobile);
        redisTemplate.opsForValue().set(cacheKey, code, 10, TimeUnit.MINUTES);
    }

    private String getCacheKey(String mobile) {
        return "user::register::code::" + mobile;
    }

    public String generateCode() {
        long nanoTime = System.nanoTime();
        String codeStr = String.valueOf(nanoTime);
        return codeStr.substring(codeStr.length() - 6);
    }
}
