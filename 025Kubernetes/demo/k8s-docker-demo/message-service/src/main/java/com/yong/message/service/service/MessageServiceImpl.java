package com.yong.message.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    public boolean sendMobileMessage(String mobile, String message) {
        logger.info("mobile: {}, message: {}", mobile, message);
        return true;
    }

    public boolean sendEmailMessage(String email, String message) {
        logger.info("email: {}, message: {}", email, message);
        return true;
    }
}
