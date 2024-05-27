package com.joy.controller.user;

import com.joy.entity.Category;
import com.joy.result.Result;
import com.joy.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("query category list")
    public Result<Category[]> getCategory(Integer type) {
        log.info("查询菜品分类...");
        Category[] list = categoryService.getList(type);
        return Result.success(list);
    }

}
