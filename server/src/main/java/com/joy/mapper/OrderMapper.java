package com.joy.mapper;

import com.github.pagehelper.Page;
import com.joy.dto.OrdersPageQueryDTO;
import com.joy.entity.Orders;
import com.joy.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    void update(Orders orders);

    Page<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    OrderVO getById(long id);

    @Select("select count(*) from orders where status = #{status}")
    int countByStatus(int status);

    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(int status, LocalDateTime orderTime);

    @Select("select * from orders where number = #{number} and user_id = #{userId}")
    Orders getByNumberAndUserId(String number, long userId);
}
