package com.yong.redis.web;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class TestController {
    private final StringRedisTemplate redisTemplate;

    public TestController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/test/write/{key}/{value}")
    public ResponseEntity<Boolean> write(@PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 60, TimeUnit.SECONDS);
        return ResponseEntity.ok(Boolean.TRUE);
    }
}
