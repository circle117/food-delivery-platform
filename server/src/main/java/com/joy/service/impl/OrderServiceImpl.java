package com.joy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.joy.constant.MessageConstant;
import com.joy.context.BaseContext;
import com.joy.dto.*;
import com.joy.entity.*;
import com.joy.exception.AddressBookBusinessException;
import com.joy.exception.OrderBusinessException;
import com.joy.exception.ShoppingCartBusinessException;
import com.joy.mapper.AddressBookMapper;
import com.joy.mapper.OrderDetailMapper;
import com.joy.mapper.OrderMapper;
import com.joy.mapper.UserMapper;
import com.joy.result.PageResult;
import com.joy.service.OrderService;
import com.joy.vo.OrderPaymentVO;
import com.joy.vo.OrderStatisticsVO;
import com.joy.vo.OrderSubmitVO;
import com.joy.vo.OrderVO;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // address is null or shopping cart is null
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null)
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);

        Map<Object, Object> entries = redisTemplate.opsForHash().entries("cart:"+ BaseContext.getCurrentId());
        if (entries == null || entries.isEmpty())
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);

        // insert one entry to the order table
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setOrderTime(LocalDateTime.now());
        order.setPayStatus(Orders.UN_PAID);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setPhone(addressBook.getPhone());
        order.setConsignee(addressBook.getConsignee());
        order.setAddress(addressBook.getDetail());
        order.setUserId(BaseContext.getCurrentId());

        orderMapper.insert(order);

        // insert several entries to the order detail table
        List<OrderDetail> list = new LinkedList<>();
        for (Object value:entries.values()) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(value, orderDetail);
            orderDetail.setOrderId(order.getId());
            list.add(orderDetail);
        }
        orderDetailMapper.insertBatch(list);

        // clean the shopping cart data
        redisTemplate.delete("cart:"+BaseContext.getCurrentId());

        // create VO
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .build();

        return orderSubmitVO;
    }

    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        // //调用微信支付接口，生成预支付交易单
        // JSONObject jsonObject = weChatPayUtil.pay(
        //         ordersPaymentDTO.getOrderNumber(), //商户订单号
        //         new BigDecimal(0.01), //支付金额，单位 元
        //         "苍穹外卖订单", //商品描述
        //         user.getOpenid() //微信用户的openid
        // );
        //
        // if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
        //     throw new OrderBusinessException("该订单已支付");
        // }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        Orders orders = Orders.builder()
                                .number(ordersPaymentDTO.getOrderNumber())
                                .checkoutTime(LocalDateTime.now())
                                .status(Orders.TO_BE_CONFIRMED)
                                .payStatus(Orders.PAID).build();
        orderMapper.update(orders);

        return vo;
    }

    @Transactional
    public PageResult getPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersPageQueryDTO, orders);

        Page<OrderVO> pageOrders = orderMapper.pageQuery(ordersPageQueryDTO);
        pageOrders.forEach(orderVO -> {
            List<String> dishNamelist = new LinkedList<>();
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderVO.getId());

            orderDetailList.forEach(orderDetail -> {
                dishNamelist.add(orderDetail.getName());
            });

            orderVO.setOrderDetailList(orderDetailList);
            orderVO.setOrderDishes(String.join(", ", dishNamelist));
        });

        PageResult pageResult = new PageResult(pageOrders.getTotal(), pageOrders.getResult());
        return pageResult;
    }

    @Transactional
    public OrderVO getByIdWithDetails(long id) {
        OrderVO orderVO = orderMapper.getById(id);
        orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(id));
        return orderVO;
    }

    public void cancel(long id) {
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.CANCELLED)
                .cancelTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);
    }

    @Transactional
    public void reorder(long id) {
        Orders orders = orderMapper.getById(id);
        orders.setOrderTime(LocalDateTime.now());
        orders.setId(null);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orderMapper.insert(orders);

        List<OrderDetail> list = orderDetailMapper.getByOrderId(id);
        for (OrderDetail orderDetail: list) {
            orderDetail.setOrderId(orders.getId());
        }
        orderDetailMapper.insertBatch(list);
    }

    @Transactional
    public OrderStatisticsVO getStatistics() {
        OrderStatisticsVO statisticsVO = new OrderStatisticsVO();
        statisticsVO.setConfirmed(orderMapper.countByStatus(Orders.CONFIRMED));
        statisticsVO.setDeliveryInProgress(orderMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS));
        statisticsVO.setToBeConfirmed(orderMapper.countByStatus(Orders.TO_BE_CONFIRMED));

        return statisticsVO;
    }

    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }

    @Transactional
    public void reject(OrdersRejectionDTO ordersRejectionDTO) {
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    public void deliver(long id) {
        Orders ordersDB = orderMapper.getById(id);

        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(orders);
    }

    public void complete(long id) {
        Orders ordersDB = orderMapper.getById(id);

        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);

    }
}
