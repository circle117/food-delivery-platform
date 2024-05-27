package com.joy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.joy.constant.MessageConstant;
import com.joy.dto.UserLoginDTO;
import com.joy.entity.User;
import com.joy.exception.LoginFailedException;
import com.joy.mapper.UserMapper;
import com.joy.properties.WeChatProperties;
import com.joy.service.UserService;
import com.joy.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN_ADDRESS = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    public User wxLogin(UserLoginDTO userLoginDTO) {
        // use wechat login API to get the openid
        String openid = getOpenid(userLoginDTO.getCode());

        // if the openid is null,  throw error
        if (openid == null)
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        // if openid exists in the user table
        User user = userMapper.getByOpenid(openid);

        // new user, register
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
        }

        // return the user entity
        return user;
    }

    private String getOpenid(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN_ADDRESS, map);

        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject.getString("openid");
    }
}
