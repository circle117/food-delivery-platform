package com.joy.mapper;

import com.joy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    @Select("select * from user where id=#{id}")
    User getById(long id);

    Integer countByMap(Map<String, Object> map);
}
