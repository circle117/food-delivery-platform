package com.joy.service;

import com.joy.dto.UserLoginDTO;
import com.joy.entity.User;

public interface UserService {

    User wxLogin(UserLoginDTO userLoginDTO);
}
