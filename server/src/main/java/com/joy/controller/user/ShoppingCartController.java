package com.joy.controller.user;

import com.joy.context.BaseContext;
import com.joy.dto.ShoppingCartDTO;
import com.joy.entity.Dish;
import com.joy.entity.Setmeal;
import com.joy.entity.ShoppingCart;
import com.joy.result.Result;
import com.joy.service.DishService;
import com.joy.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "user shopping cart API")
public class ShoppingCartController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @PostMapping("/add")
    @ApiOperation("add dish into the shopping cart")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车，菜品信息为: {}", shoppingCartDTO);

        String key = "cart:" + BaseContext.getCurrentId();
        String hashKey;

        // check if it exists in the map
        ShoppingCart shoppingCart;
        if (shoppingCartDTO.getDishId() != null)
            hashKey = String.join("-", "d", String.valueOf(shoppingCartDTO.getDishId()), shoppingCartDTO.getDishFlavor());
        else
            hashKey = String.join("-", "s", String.valueOf(shoppingCartDTO.getSetmealId()));
        shoppingCart = (ShoppingCart) redisTemplate.opsForHash().get(key, hashKey);

        // if exists, update the number
        if (shoppingCart != null) {
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            redisTemplate.opsForHash().put(key, hashKey, shoppingCart);
        }
        else {
            shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
            // if not exist, add the dish/setmeal item
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // dish
                Dish dish = dishService.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // setmeal
                Setmeal setmeal = setmealService.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);

            redisTemplate.opsForHash().put(key, hashKey, shoppingCart);

        }

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("query the dishes in the shopping cart")
    public Result<List<ShoppingCart>> getList() {
        log.info("查看购物车: {}", BaseContext.getCurrentId());
        String key = "cart:" + BaseContext.getCurrentId();

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        List<ShoppingCart> list = new LinkedList<>();
        for (Object value:entries.values()) {
            list.add((ShoppingCart) value);
        }

        return Result.success(list);
    }

    @DeleteMapping("/clean")
    @ApiOperation("delete the dishes in the shopping cart")
    public Result delete() {
        log.info("清空购物车: {}", BaseContext.getCurrentId());
        String key = "cart:" + BaseContext.getCurrentId();
        redisTemplate.delete(key);
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("subtract the dish in the shopping cart")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("从购物车删减商品: {}", shoppingCartDTO);
        String key = "cart:" + BaseContext.getCurrentId();
        String hashKey;
        if (shoppingCartDTO.getDishId() != null) {
            hashKey = String.join("-", "d", String.valueOf(shoppingCartDTO.getDishId()), shoppingCartDTO.getDishFlavor());
        } else {
            hashKey = String.join("-", "d", String.valueOf(shoppingCartDTO.getSetmealId()));
        }

        ShoppingCart shoppingCart = (ShoppingCart) redisTemplate.opsForHash().get(key, hashKey);
        if (shoppingCart.getNumber() == 1) {
            redisTemplate.opsForHash().delete(key, hashKey);
        } else {
            shoppingCart.setNumber(shoppingCart.getNumber()-1);
            redisTemplate.opsForHash().put(key, hashKey, shoppingCart);
        }

        return Result.success();
    }
}
