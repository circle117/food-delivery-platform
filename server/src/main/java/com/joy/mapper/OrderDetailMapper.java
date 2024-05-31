package com.joy.mapper;

import com.joy.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    void insertBatch(List<OrderDetail> list);

    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(long orderId);
}
