package com.joy.controller.admin;

import com.joy.dto.OrdersCancelDTO;
import com.joy.dto.OrdersConfirmDTO;
import com.joy.dto.OrdersPageQueryDTO;
import com.joy.dto.OrdersRejectionDTO;
import com.joy.result.PageResult;
import com.joy.result.Result;
import com.joy.service.OrderService;
import com.joy.vo.OrderStatisticsVO;
import com.joy.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Api(tags = "Order API for admin")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("search for orders")
    public Result<PageResult> search(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.getPage(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("calculate the number of different order status")
    public Result<OrderStatisticsVO> getStatistics() {
        log.info("订单状态数量统计");
        OrderStatisticsVO statisticsVO = orderService.getStatistics();
        return Result.success(statisticsVO);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("get the order details")
    public Result<OrderVO> getByIdWithDetails(@PathVariable long id) {
        log.info("查询订单详情: {}", id);
        return Result.success(orderService.getByIdWithDetails(id));
    }

    @PutMapping("/confirm")
    @ApiOperation("confirm the order")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单: {}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("reject the order")
    public Result confirm(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("接单: {}", ordersRejectionDTO);
        orderService.reject(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("cancel the order")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单: {}", ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("deliver the order")
    public Result deliver(@PathVariable long id) {
        log.info("派送订单: {}", id);
        orderService.deliver(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("deliver the order")
    public Result complete(@PathVariable long id) {
        log.info("完成订单: {}", id);
        orderService.complete(id);
        return Result.success();
    }
}
