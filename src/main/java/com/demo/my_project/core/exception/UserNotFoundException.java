package com.demo.my_project.core.exception;

import java.text.MessageFormat;

/**
 * leap示例, 可删除.
 * Created by Leap maven archetype.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super(MessageFormat.format("找不到id为 {} 的用户!", userId));
    }
}
