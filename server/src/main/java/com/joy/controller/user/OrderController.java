package com.joy.controller.user;

import com.joy.dto.OrdersPageQueryDTO;
import com.joy.dto.OrdersPaymentDTO;
import com.joy.dto.OrdersSubmitDTO;
import com.joy.result.PageResult;
import com.joy.result.Result;
import com.joy.service.OrderService;
import com.joy.vo.OrderPaymentVO;
import com.joy.vo.OrderSubmitVO;
import com.joy.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags="order API")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("user submit the order")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    @ApiOperation("pay the order")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付: {}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        return Result.success(orderPaymentVO);
    }

    @GetMapping("/historyOrders")
    @ApiOperation("get the history orders")
    public Result<PageResult> getPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("历史订单查询...");
        PageResult pageResult = orderService.getPage(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("check the order details")
    public Result<OrderVO> getByIdWithDetails(@PathVariable long id) {
        log.info("查询订单详情: {}", id);
        OrderVO orderVO = orderService.getByIdWithDetails(id);

        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("cancel the order")
    public Result cancel(@PathVariable long id) {
        log.info("取消订单: {}", id);
        orderService.cancel(id);
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    @ApiOperation("order again")
    public Result reorder(@PathVariable long id) {
        log.info("再来一单: {}", id);
        orderService.reorder(id);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    @ApiOperation("remind the order")
    public Result reminder(@PathVariable long id) {
        log.info("用户催单: {}", id);
        orderService.reminder(id);
        return Result.success();
    }
}
