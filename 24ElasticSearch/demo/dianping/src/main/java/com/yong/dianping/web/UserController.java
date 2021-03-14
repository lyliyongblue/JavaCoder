package com.yong.dianping.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@RequestMapping("/user")
public class UserController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
