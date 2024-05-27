package com.joy.controller.user;

import com.joy.entity.Setmeal;
import com.joy.entity.SetmealDish;
import com.joy.result.Result;
import com.joy.service.SetmealService;
import com.joy.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "API for setmeals")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/list")
    @ApiOperation("search for setmeals by category id")
    public Result<List<Setmeal>> getByCategoryId(long categoryId) {
        log.info("根据分类id查询套餐: {}", categoryId);
        List<Setmeal> list = setmealService.getByCategoryId(categoryId);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    @ApiOperation("search for setmeal dish")
    public Result<List<DishItemVO>> getSetmealDishBySetmealId(@PathVariable long id) {
        log.info("根据套餐id查询包含的菜品: {}", id);
        List<DishItemVO> list = setmealService.getDishItemBySetmealId(id);
        return Result.success(list);
    }
}
