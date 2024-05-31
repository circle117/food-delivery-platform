package com.joy.mapper;

import com.github.pagehelper.Page;
import com.joy.dto.OrdersPageQueryDTO;
import com.joy.entity.Orders;
import com.joy.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    void update(Orders orders);

    Page<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    OrderVO getById(long id);

    @Select("select count(*) from orders where status = #{status}")
    int countByStatus(int status);
}
