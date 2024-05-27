package com.joy.controller.admin;

import com.joy.dto.DishDTO;
import com.joy.dto.DishPageQueryDTO;
import com.joy.entity.Dish;
import com.joy.result.PageResult;
import com.joy.result.Result;
import com.joy.service.DishService;
import com.joy.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Api(tags = "dish API")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping()
    @ApiOperation("add new dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("get dish list")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping()
    @ApiOperation("delete dishes")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("get one dish")
    public Result<DishVO> getById(@PathVariable long id) {
        log.info("查询菜品: {}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping()
    @ApiOperation("update one dish")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("enable or disable a dish")
    public Result enableOrDisable(@PathVariable int status, long id) {
        log.info("修改菜品状态: {}", id);
        dishService.enableOrDisable(status, id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("get dish by categoryId")
    public Result<List<Dish>> getDishList(long categoryId) {
        log.info("根据分类id查询菜品: {}", categoryId);
        List<Dish> dishList = dishService.getListByCategoryId(categoryId);
        return Result.success(dishList);
    }
}
