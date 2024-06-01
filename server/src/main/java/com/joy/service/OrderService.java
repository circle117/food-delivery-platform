package com.joy.service;

import com.joy.dto.*;
import com.joy.result.PageResult;
import com.joy.vo.OrderPaymentVO;
import com.joy.vo.OrderStatisticsVO;
import com.joy.vo.OrderSubmitVO;
import com.joy.vo.OrderVO;

public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getByIdWithDetails(long id);

    void cancel(long id);

    void reorder(long id);

    OrderStatisticsVO getStatistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void reject(OrdersRejectionDTO ordersRejectionDTO);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    void deliver(long id);

    void complete(long id);

    void reminder(long id);
}
