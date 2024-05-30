package com.joy.service;

import com.joy.dto.OrdersPaymentDTO;
import com.joy.dto.OrdersSubmitDTO;
import com.joy.vo.OrderPaymentVO;
import com.joy.vo.OrderSubmitVO;

public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
}
