package com.joy.mapper;

import com.joy.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    void update(Orders orders);
}
