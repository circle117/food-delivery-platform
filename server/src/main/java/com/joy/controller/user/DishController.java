package com.joy.controller.user;

import com.joy.constant.StatusConstant;
import com.joy.entity.Dish;
import com.joy.result.Result;
import com.joy.service.DishService;
import com.joy.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    @ApiOperation("query dish by category id")
    public Result<List<DishVO>> getDishByCategoryId(long categoryId) {
        log.info("根据分类id查询菜品...");

        String key = "category:" + categoryId;

        // check whether the list data exists in Redis
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);

        // if exists, return the data in Redis
        if (list != null && !list.isEmpty()) {
            return Result.success(list);
        }

        // if not exists, put the data into the Redis
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        list = dishService.getListWithFlavor(dish);
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }
}
