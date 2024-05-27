package com.joy.controller.admin;

import com.joy.dto.SetmealDTO;
import com.joy.dto.SetmealPageQueryDTO;
import com.joy.result.PageResult;
import com.joy.result.Result;
import com.joy.service.SetmealService;
import com.joy.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags="API for setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("save a new setmeal")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐: {}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("query the setmeal page")
    public Result<PageResult> queryPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询: {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.queryPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("delete one setmeal")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("删除套餐: {}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("get one setmeal")
    public Result<SetmealVO> getById(@PathVariable long id) {
        log.info("查询套餐: {}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDishes(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("update one setmeal")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐信息: {}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("enable or disable one setmeal")
    public Result enableOrDisable(@PathVariable int status, long id) {
        log.info("起售停售套餐: {}", id);
        setmealService.enableOrDisable(status, id);
        return Result.success();
    }
}
