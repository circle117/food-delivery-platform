package com.joy.controller.admin;

import com.joy.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "API for restaurants")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("set the restaurant status")
    public Result setStatus(@PathVariable int status) {
        log.info("设置店铺营业状态: {}", status == 1? "营业中": "已打烊");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("query the restaurant status")
    public Result<Integer> getStatus() {
        log.info("获取店铺营业状态...");
        Integer status = (Integer)redisTemplate.opsForValue().get(KEY);
        return Result.success(status);
    }
}
